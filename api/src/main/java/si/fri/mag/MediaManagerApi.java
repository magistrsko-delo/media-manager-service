package si.fri.mag;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import si.fri.mag.controllers.MainController;
import si.fri.mag.controllers.RootController;
import si.fri.mag.controllers.v1.MediaManagerController;
import si.fri.mag.mappers.ForbiddenExceptionMapper;
import si.fri.mag.mappers.InternalServerErrorExceptionMapper;
import si.fri.mag.mappers.NotFoundExceptionMapper;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class MediaManagerApi extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(MultiPartFeature.class);
        resources.add(RootController.class);
        resources.add(MainController.class);
        resources.add(MediaManagerController.class);
        resources.add(ForbiddenExceptionMapper.class);
        resources.add(InternalServerErrorExceptionMapper.class);
        resources.add(NotFoundExceptionMapper.class);
        return resources;
    }
}
