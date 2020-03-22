package si.fri.mag;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.mp4parser.IsoFile;
import si.fri.DTO.NewMediaMetadata;
import si.fri.DTO.requests.NewMediaResponseData;
import si.fri.mag.utils.RabbitMQService;
import si.fri.mag.utils.RequestSenderService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.SplittableRandom;

@RequestScoped
public class MediaManagerService {

    @Inject
    private RequestSenderService requestSenderService;

    @Inject
    private RabbitMQService rabbitMQService;

    public NewMediaResponseData uploadAndCreateMedia(InputStream uploadedInputStream, FormDataContentDisposition mediaDetails,
                                        String siteName, String mediaName)  {

        Integer mediaLength = 0;
        File media = new File(mediaDetails.getFileName());
        try {
            FileUtils.copyInputStreamToFile(uploadedInputStream, media);
            IsoFile isoFile = new IsoFile(mediaDetails.getFileName());
            double lengthInSeconds = (int)((double) isoFile.getMovieBox().getMovieHeaderBox().getDuration() / isoFile.getMovieBox().getMovieHeaderBox().getTimescale());
            mediaLength = new Double(lengthInSeconds).intValue();
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

        String bucketName = requestSenderService.createNewBucketForMedia(mediaName.toLowerCase().replaceAll("\\s+","-"));
        requestSenderService.sendMediaToUploadOnS3(media, bucketName, mediaDetails.getFileName());

        boolean isMediaDeleted = media.delete();
        if(!isMediaDeleted){
            System.out.println("File was not deleted!");
        }

        // CREATE NEW MEDIA METADATA
        NewMediaResponseData newCreatedMedia = requestSenderService.createNewMediaMetadata(
                new NewMediaMetadata(mediaName, siteName, mediaLength, 0, bucketName, mediaDetails.getFileName())
        );

        Gson gson = new Gson();
        rabbitMQService.sendMessage(gson.toJson(newCreatedMedia));

        return newCreatedMedia;
    }

}
