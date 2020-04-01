package si.fri.mag.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import si.fri.mag.AWSRemoteServiceConfig;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.io.InputStream;

@RequestScoped
public class RequestSenderService {

    @Inject
    private AWSRemoteServiceConfig awsRemoteServiceConfig;

    private Client httpClient;

    @PostConstruct
    private void init(){
        this.httpClient = ClientBuilder.newClient();
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
}
