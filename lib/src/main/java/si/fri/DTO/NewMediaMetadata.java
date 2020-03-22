package si.fri.DTO;

public class NewMediaMetadata extends MediaMetadataAbstract {

    public NewMediaMetadata(String name, String siteName, Integer length, Integer status, String awsBucketWholeMedia, String awsStorageNameWholeMedia) {
        this.name = name;
        this.siteName = siteName;
        this.length = length;
        this.status = status;
        this.awsBucketWholeMedia = awsBucketWholeMedia;
        this.awsStorageNameWholeMedia = awsStorageNameWholeMedia;
    }

}
