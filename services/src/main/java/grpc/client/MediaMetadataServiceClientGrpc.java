package grpc.client;

import com.kumuluz.ee.grpc.client.GrpcChannelConfig;
import com.kumuluz.ee.grpc.client.GrpcChannels;
import com.kumuluz.ee.grpc.client.GrpcClient;
import grpc.AwsstorageGrpc;
import grpc.MediaMetadataGrpc;
import grpc.MediametadataService;
import si.fri.DTO.NewMediaMetadata;
import si.fri.DTO.requests.NewMediaResponseData;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLException;
import javax.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class MediaMetadataServiceClientGrpc {

    private MediaMetadataGrpc.MediaMetadataBlockingStub mediaMetadataBlockingStub;

    @PostConstruct
    public void init() {
        try {
            GrpcChannels clientPool = GrpcChannels.getInstance();
            GrpcChannelConfig config = clientPool.getGrpcClientConfig("mediaMetadataClient");
            GrpcClient client = new GrpcClient(config);
            mediaMetadataBlockingStub = MediaMetadataGrpc.newBlockingStub(client.getChannel());
        } catch (SSLException e) {
            System.out.println(e.getMessage());
        }
    }

    public NewMediaResponseData createNewMediaMetadata(NewMediaMetadata newMediaMetadata) {
        MediametadataService.CreateNewMediaMetadataRequest request = MediametadataService.CreateNewMediaMetadataRequest
                .newBuilder()
                .setName(newMediaMetadata.getName())
                .setSiteName(newMediaMetadata.getSiteName())
                .setLength(newMediaMetadata.getLength())
                .setStatus(newMediaMetadata.getStatus())
                .setThumbnail("")
                .setProjectId(-1)
                .setAwsBucketWholeMedia(newMediaMetadata.getAwsBucketWholeMedia())
                .setAwsStorageNameWholeMedia(newMediaMetadata.getAwsStorageNameWholeMedia())
                .build();

        try {
            MediametadataService.MediaMetadataResponse rs = mediaMetadataBlockingStub.newMediaMetadata(request);
            NewMediaResponseData newMediaResponseData = new NewMediaResponseData();
            newMediaResponseData.setMediaId(rs.getMediaId());
            newMediaResponseData.setName(rs.getName());
            newMediaResponseData.setSiteName(rs.getSiteName());
            newMediaResponseData.setLength(rs.getLength());
            newMediaResponseData.setThumbnail(rs.getThumbnail());
            newMediaResponseData.setProjectId(rs.getProjectId());
            newMediaResponseData.setAwsBucketWholeMedia(rs.getAwsBucketWholeMedia());
            newMediaResponseData.setAwsStorageNameWholeMedia(rs.getAwsStorageNameWholeMedia());
            newMediaResponseData.setKeywords(rs.getKeywordsList() );
            newMediaResponseData.setCreatedAt (String.valueOf(rs.getCreatedAt()));
            newMediaResponseData.setUpdatedAt(String.valueOf(rs.getUpdatedAt()));
            return newMediaResponseData;
        } catch (Exception e) {
            throw new InternalServerErrorException("new media metadata: " + e.getMessage());
        }
    }
}
