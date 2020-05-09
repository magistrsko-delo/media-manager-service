package si.fri.mag.controllers.v1;

import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import si.fri.DTO.requests.MediaImageRequest;
import si.fri.DTO.requests.NewMediaResponseData;
import si.fri.mag.MediaManagerService;
import si.fri.mag.controllers.MainController;
import si.fri.mag.utils.RequestSenderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@ApplicationScoped
@Path("/v1/mediaManager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MediaManagerController extends MainController {

    @Inject
    private MediaManagerService mediaManagerService;

    @Inject
    private RequestSenderService requestSenderService;

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadMedia(
            @NotEmpty
            @FormDataParam("mediaStream") InputStream mediaStream,
            @FormDataParam("mediaStream") FormDataContentDisposition mediaDetails,
            @FormDataParam("siteName") String siteName,
            @FormDataParam("mediaName") String mediaName
    ) {

        NewMediaResponseData newMedia = mediaManagerService.uploadAndCreateMedia(mediaStream, mediaDetails, siteName, mediaName);
        return this.responseOk("", newMedia);
    }

    @POST
    @Path("{mediaId}/image")
    public Response mediaImage(@PathParam("mediaId") Integer mediaId, String body) {

        Gson gson = new Gson();
        MediaImageRequest mediaImageRequest;

        try {
            mediaImageRequest = gson.fromJson(body, MediaImageRequest.class);
        } catch (Exception e) {
            return this.responseError(500, e.getMessage());
        }

        if (!mediaImageRequest.getMediaId().equals(mediaId)) {
            return this.responseError(400, "mediaIds doues not match");
        }

        boolean isImageSendToWorkQueue = mediaManagerService.sendMediaImageToWorkQueue(mediaImageRequest);

        if (!isImageSendToWorkQueue) {
            return this.responseError(404, "Media with id: " + mediaId + " does not exist");
        }

        return this.responseOk("Media image send to work queue", isImageSendToWorkQueue);
    }

    @DELETE
    @Path("{mediaId}")
    public Response deleteMedia(@PathParam("mediaId") Integer mediaId) {
        boolean isMediaDeleted = mediaManagerService.deleteMedia(mediaId);

        if (!isMediaDeleted) {
            this.responseError(500, "media not deleted");
        }
        return this.responseOk("media deleted", "ok");
    }

    @GET
    @Path("{bucketName}/{mediaName}")
    public Response getWholeMedia(@PathParam("bucketName") String bucketName, @PathParam("mediaName") String mediaName,
                                  @QueryParam("isImage") boolean isImage) {

        InputStream mediaObject = requestSenderService.getMedia(bucketName, mediaName);
        Response.ResponseBuilder response = Response.ok(mediaObject);

        if (isImage) {
            response.type("image/jpg");
            response.header("Content-Disposition", "filename="+mediaName);
        } else {
            response.header("Content-Disposition", "attachment; filename="+mediaName);
        }

        return response.build();
    }

}
