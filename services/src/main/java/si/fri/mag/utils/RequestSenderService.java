package si.fri.mag.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.microprofile.opentracing.ClientTracingRegistrar;
import si.fri.mag.AWSRemoteServiceConfig;
import si.fri.mag.MediaChunksRemoteServiceConfig;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@RequestScoped
public class RequestSenderService {

    @Inject
    private AWSRemoteServiceConfig awsRemoteServiceConfig;

    @Inject
    private MediaChunksRemoteServiceConfig mediaChunksRemoteServiceConfig;

    private Client httpClient;

    @PostConstruct
    private void init(){
        this.httpClient = ClientTracingRegistrar.configure(ClientBuilder.newBuilder()).build();
    }

    public InputStream getMedia(String bucketName, String mediaName){
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(awsRemoteServiceConfig.getAwsStorageUrl() + awsRemoteServiceConfig.getGetMediaUri() + bucketName + "/" + mediaName);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                return entity.getContent();
            } else {
                return null;
            }
        } catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public boolean deleteLinkedMediaChunks(Integer mediaId) {
        // System.out.println(this.mediaChunksRemoteServiceConfig.getMediaChunksUrl() + "v1/media/" + mediaId);
        try {
            Response response = httpClient
                    .target(this.mediaChunksRemoteServiceConfig.getMediaChunksUrl() + "v1/media/" + mediaId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .delete();
            return response.getStatus() == 200;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
