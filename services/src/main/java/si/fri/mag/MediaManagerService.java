package si.fri.mag;

import com.google.gson.Gson;
import grpc.AwsstorageService;
import grpc.client.AwsStorageServiceClientGrpc;
import grpc.client.MediaMetadataServiceClientGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.mp4parser.IsoFile;
import si.fri.DTO.NewMediaMetadata;
import si.fri.DTO.VideoAnalysisQueueMessage;
import si.fri.DTO.requests.MediaImageRequest;
import si.fri.DTO.requests.NewMediaResponseData;
import si.fri.mag.utils.RabbitMQService;
import si.fri.mag.utils.RequestSenderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class MediaManagerService {

    @Inject
    private RequestSenderService requestSenderService;

    @Inject
    private RabbitMQService rabbitMQService;

    @Inject
    private AwsStorageServiceClientGrpc awsStorageServiceClientGrpc;


    @Inject
    private MediaMetadataServiceClientGrpc mediaMetadataServiceClientGrpc;

    private NewMediaResponseData newCreatedMedia;

    public NewMediaResponseData uploadAndCreateMedia(InputStream uploadedInputStream, FormDataContentDisposition mediaDetails,
                                        String siteName, String mediaName)  {

        Integer mediaLength = 0;
        File  mediaFile = new File(mediaDetails.getFileName());
        try {
            FileUtils.copyInputStreamToFile(uploadedInputStream, mediaFile);
            IsoFile isoFile = new IsoFile(mediaDetails.getFileName());
            double lengthInSeconds = (int)((double) isoFile.getMovieBox().getMovieHeaderBox().getDuration() / isoFile.getMovieBox().getMovieHeaderBox().getTimescale());
            mediaLength = new Double(lengthInSeconds).intValue();
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        String bucketName = awsStorageServiceClientGrpc.createAwsBucket(mediaName.toLowerCase().replaceAll("\\s+","-"));

        awsStorageServiceClientGrpc.uploadFileToAWS(mediaFile, bucketName, mediaDetails.getFileName());

        System.out.println("METADATA");
        // CREATE NEW MEDIA METADATA
        newCreatedMedia = mediaMetadataServiceClientGrpc.createNewMediaMetadata(
                new NewMediaMetadata(mediaName, siteName, mediaLength, 0, bucketName, mediaDetails.getFileName())
        );

        return newCreatedMedia;
    }

    public StreamObserver<AwsstorageService.UploadResponse> uploadStreamGrpcObserver() {
        return new StreamObserver<AwsstorageService.UploadResponse>() {

            @Override
            public void onNext(AwsstorageService.UploadResponse value) {
                System.out.println("SENDING TO WORKER: " + newCreatedMedia.getMediaId());
                VideoAnalysisQueueMessage videoAnalysisQueueMessage = new VideoAnalysisQueueMessage();
                videoAnalysisQueueMessage.setMediaId(newCreatedMedia.getMediaId());

                Gson gson = new Gson();
                rabbitMQService.sendChunksQueueMessage(gson.toJson(newCreatedMedia));
                rabbitMQService.sendVideoAnalysisQueueMessage(gson.toJson(videoAnalysisQueueMessage));
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Client response onError: ");
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Client response onCompleted");
                final File file = new File(newCreatedMedia.getAwsStorageNameWholeMedia());
                file.delete();
            }
        };
    }

    public boolean sendMediaImageToWorkQueue(MediaImageRequest mediaImageRequest) {

        boolean isMedia = mediaMetadataServiceClientGrpc.checkIfMediaExist(mediaImageRequest.getMediaId());

        if (!isMedia) {
            return false;
        }

        Gson gson = new Gson();
        rabbitMQService.sendImageQueueMessage(gson.toJson(mediaImageRequest));
        return true;
    }

    public boolean deleteMedia(Integer mediaId) {
        boolean isMediaDeleted = mediaMetadataServiceClientGrpc.deleteMediaMetadata(mediaId);
        if (!isMediaDeleted) {
            System.out.println("problem with media deletion");
            return false;
        }

        boolean isLinkMediaChunkDeleted = requestSenderService.deleteLinkedMediaChunks(mediaId);
        if (!isLinkMediaChunkDeleted) {
            System.out.println("link media chunk deletion problem");
            return false;
        }

        return true;
    }

}
