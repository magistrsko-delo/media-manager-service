package si.fri.mag.utils;

import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.Boundary;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import si.fri.DTO.NewMediaMetadata;
import si.fri.DTO.requests.AWSNewBucket;
import si.fri.DTO.requests.NewMediaResponseData;
import si.fri.DTO.requests.core.AWSRequestResponse;
import si.fri.DTO.requests.core.NewMediaResponse;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

@RequestScoped
public class RequestSenderService {

    private Client httpClient;

    @PostConstruct
    private void init(){
        this.httpClient = ClientBuilder.newClient();
    }

    public boolean sendMediaToUploadOnS3(File newMedia, String bucketName, String mediaStorageName) {
        MultiPart multiPart = null;
        String serverURL = "http://localhost:8002/v1/awsStorage/media" + "/" + bucketName + "/" + mediaStorageName; // TODO
        System.out.println(serverURL);
        try {
            Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
            WebTarget server = client.target(serverURL);

            multiPart = new MultiPart();
            FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("mediaStream", newMedia, MediaType.APPLICATION_OCTET_STREAM_TYPE);
            multiPart.bodyPart(fileDataBodyPart);

            MediaType contentType = MediaType.MULTIPART_FORM_DATA_TYPE;
            contentType = Boundary.addBoundary(contentType);

            Response response = server
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post( Entity.entity(multiPart, contentType));
            if (response.getStatus() == 200) {
                return true;
            } else {
                throw new InternalServerErrorException("error when uploading media on bucket: STATUS: " + response.getStatus());
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            if (null != multiPart) {
                try {
                    multiPart.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String createNewBucketForMedia(String bucketName){
        System.out.println("CreatingBucket url: " + "http://localhost:8002/v1/awsStorage/bucket/" + bucketName);  //TODO
        try{
            Response response = httpClient
                    .target("http://localhost:8002/v1/awsStorage/bucket/" + bucketName)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));

            if (response.getStatus() == 200) {
                Gson gson = new Gson();
                String responseBody = response.readEntity(String.class);
                AWSNewBucket awsNewBucket = (AWSNewBucket) gson.fromJson(responseBody, AWSRequestResponse.class).getData();
                return awsNewBucket.getName();
            } else {
                throw new InternalServerErrorException("error when creating aws bucket: STATUS: " + response.getStatus());
            }
        }catch (WebApplicationException | ProcessingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public NewMediaResponseData createNewMediaMetadata(NewMediaMetadata newMediaMetadata){

        System.out.println("Media metadata " + "http://localhost:8001/v1/media/metadata/new");  //TODO
        try{
            Response response = httpClient
                    .target("http://localhost:8001/v1/media/metadata/new")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(newMediaMetadata, MediaType.APPLICATION_JSON_TYPE));

            if (response.getStatus() == 200) {
                Gson gson = new Gson();
                String responseBody = response.readEntity(String.class);
                return  (NewMediaResponseData) gson.fromJson(responseBody, NewMediaResponse.class).getData();
            } else {
                throw new InternalServerErrorException("error when creating aws bucket: STATUS: " + response.getStatus());
            }
        }catch (WebApplicationException | ProcessingException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }


}
