package grpc.client;

import com.kumuluz.ee.grpc.client.GrpcChannelConfig;
import com.kumuluz.ee.grpc.client.GrpcChannels;
import com.kumuluz.ee.grpc.client.GrpcClient;
import grpc.AwsstorageGrpc;
import grpc.AwsstorageService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLException;
import javax.ws.rs.InternalServerErrorException;

@ApplicationScoped
public class AwsStorageServiceClientGrpc {
    private AwsstorageGrpc.AwsstorageBlockingStub awsstorageBlockingStub;

    @PostConstruct
    public void init() {
        try {
            GrpcChannels clientPool = GrpcChannels.getInstance();
            GrpcChannelConfig config = clientPool.getGrpcClientConfig("awsStorageClient");
            GrpcClient client = new GrpcClient(config);
            awsstorageBlockingStub = AwsstorageGrpc.newBlockingStub(client.getChannel());
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
            throw new InternalServerErrorException(e.getMessage());
        }
    }

}
