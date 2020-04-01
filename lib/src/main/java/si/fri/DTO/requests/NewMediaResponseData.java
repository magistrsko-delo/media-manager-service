package si.fri.DTO.requests;

import si.fri.DTO.MediaMetadataAbstract;

import java.util.List;

public class NewMediaResponseData extends MediaMetadataAbstract {
    private Integer mediaId;
    private String thumbnail;
    private Integer projectId;
    private List<String> keywords;
    private String createdAt;
    private String updatedAt;

    public Integer getMediaId() {
        return mediaId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
