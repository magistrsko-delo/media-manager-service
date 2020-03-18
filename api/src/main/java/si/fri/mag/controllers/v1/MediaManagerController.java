package si.fri.mag.controllers.v1;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import si.fri.mag.MediaManagerService;
import si.fri.mag.controllers.MainController;

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

        boolean uploadResponse = mediaManagerService.uploadAndCreateMedia(mediaStream, mediaDetails, siteName, mediaName);

        return this.responseOk("", "ok");
    }

    @DELETE
    @Path("{mediaId}")
    public Response deleteMedia(@PathParam("mediaId") String mediaId) {
        return this.responseOk("", "ok");
    }

    @GET
    @Path("{mediaId}")
    public Response getWholeMedia(@PathParam("mediaId") String mediaId) {
        return this.responseOk("", "ok");
    }

}
