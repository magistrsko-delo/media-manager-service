package si.fri.DTO.requests;

public class MediaImageRequest {
    private Integer mediaId;
    private float time;

    public Integer getMediaId() {
        return mediaId;
    }

    public float getTime() {
        return time;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
