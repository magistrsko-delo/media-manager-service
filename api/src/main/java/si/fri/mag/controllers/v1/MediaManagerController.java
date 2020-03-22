package si.fri.mag.controllers.v1;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import si.fri.DTO.requests.NewMediaResponseData;
import si.fri.mag.MediaManagerService;
import si.fri.mag.controllers.MainController;
import si.fri.mag.utils.RequestSenderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    @DELETE
    @Path("{mediaId}")
    public Response deleteMedia(@PathParam("mediaId") String mediaId) {
        return this.responseOk("", "ok");
    }

    @GET
    @Path("{bucketName}/{mediaName}")
    public Response getWholeMedia(@PathParam("bucketName") String bucketName, @PathParam("mediaName") String mediaName) {

        InputStream mediaObject = requestSenderService.getMedia(bucketName, mediaName);

        Response.ResponseBuilder response = Response.ok(mediaObject);
        response.header("Content-Disposition", "attachment; filename="+mediaName);
        return response.build();
    }

}
