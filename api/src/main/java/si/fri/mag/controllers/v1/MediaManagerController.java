package si.fri.mag.controllers.v1;

import si.fri.mag.controllers.MainController;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/v1/mediaManager")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MediaManagerController extends MainController {

    @POST
    @Path("upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadMedia() {
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
