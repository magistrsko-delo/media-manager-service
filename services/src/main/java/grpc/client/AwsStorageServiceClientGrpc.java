package grpc.client;

import com.google.protobuf.ByteString;
import com.kumuluz.ee.grpc.client.GrpcChannelConfig;
import com.kumuluz.ee.grpc.client.GrpcChannels;
import com.kumuluz.ee.grpc.client.GrpcClient;
import grpc.AwsstorageGrpc;
import grpc.AwsstorageService;
import io.grpc.stub.StreamObserver;
import si.fri.mag.MediaManagerService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.net.ssl.SSLException;
import javax.ws.rs.InternalServerErrorException;
import java.io.*;

@ApplicationScoped
public class AwsStorageServiceClientGrpc {
    private AwsstorageGrpc.AwsstorageBlockingStub awsstorageBlockingStub;
    private AwsstorageGrpc.AwsstorageStub awsstorageStub;
    @PostConstruct
    public void init() {
        try {
            GrpcChannels clientPool = GrpcChannels.getInstance();
            GrpcChannelConfig config = clientPool.getGrpcClientConfig("awsStorageClient");
            GrpcClient client = new GrpcClient(config);
            awsstorageBlockingStub = AwsstorageGrpc.newBlockingStub(client.getChannel());
            awsstorageStub = AwsstorageGrpc.newStub(client.getChannel());
        } catch (SSLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String createAwsBucket(String bucketName) {

        AwsstorageService.CreateBucketRequest request = AwsstorageService.CreateBucketRequest
                .newBuilder()
                .setBucketname(bucketName)
                .build();

        try {
            AwsstorageService.CreateBucketResponse rs = awsstorageBlockingStub.createBucket(request);
            return rs.getBucketname();
        } catch (Exception e) {
            throw new InternalServerErrorException("AWS bucket: " + e.getMessage());
        }
    }

    public void uploadFileToAWS(File newMedia, String bucketName, String mediaName) {

        MediaManagerService mediaManagerService = CDI.current().select(MediaManagerService.class).get();

        StreamObserver<AwsstorageService.UploadRequest> requestObserver = awsstorageStub.uploadFile(mediaManagerService.uploadStreamGrpcObserver());
        try {
            try {
                BufferedInputStream bInputStream = new BufferedInputStream(new FileInputStream(newMedia));
                int bufferSize = 512 * 1024; // 512k
                byte[] buffer = new byte[bufferSize];
                int size = 0;
                while ((size = bInputStream.read(buffer)) > 0) {
                    ByteString byteString = ByteString.copyFrom(buffer, 0, size);
                    AwsstorageService.UploadRequest req = AwsstorageService.UploadRequest.newBuilder()
                            .setBucketname(bucketName)
                            .setMedianame(mediaName)
                            .setData(byteString)
                            .setOffset(size).build();
                    requestObserver.onNext(req);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        requestObserver.onCompleted();

    }

}
