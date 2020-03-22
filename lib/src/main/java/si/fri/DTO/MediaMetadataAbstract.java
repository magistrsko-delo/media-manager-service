package si.fri.DTO;

public abstract class MediaMetadataAbstract {
    protected String name;
    protected String siteName;
    protected Integer length;
    protected Integer status;
    protected String awsBucketWholeMedia;
    protected String awsStorageNameWholeMedia;

    public String getName() {
        return name;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getLength() {
        return length;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getAwsBucketWholeMedia() {
        return awsBucketWholeMedia;
    }

    public String getAwsStorageNameWholeMedia() {
        return awsStorageNameWholeMedia;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAwsBucketWholeMedia(String awsBucketWholeMedia) {
        this.awsBucketWholeMedia = awsBucketWholeMedia;
    }

    public void setAwsStorageNameWholeMedia(String awsStorageNameWholeMedia) {
        this.awsStorageNameWholeMedia = awsStorageNameWholeMedia;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
