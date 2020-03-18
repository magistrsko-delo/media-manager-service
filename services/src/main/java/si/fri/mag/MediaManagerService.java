package si.fri.mag;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.mp4parser.IsoFile;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.InternalServerErrorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RequestScoped
public class MediaManagerService {


    public boolean uploadAndCreateMedia(InputStream uploadedInputStream, FormDataContentDisposition mediaDetails,
                                        String siteName, String mediaName)  {

        System.out.println("MEDIA name: " + mediaDetails.getName());
        System.out.println("MEDIA fileName: " + mediaDetails.getFileName());
        System.out.println("MEDIA type: " + mediaDetails.getType());
        System.out.println("MEDIA size: " + mediaDetails.getSize());
        System.out.println("MEDIA parameters: " + mediaDetails.getParameters());
        try {
            File file = new File(mediaDetails.getFileName());
            System.out.println("Input stream: " + uploadedInputStream.available());
            FileUtils.copyInputStreamToFile(uploadedInputStream, file);

            IsoFile isoFile = new IsoFile(mediaDetails.getFileName());
            double lengthInSeconds = (double) isoFile.getMovieBox().getMovieHeaderBox().getDuration() / isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
            System.out.println("Media length: " + lengthInSeconds);
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }


        return true;
    }

}
