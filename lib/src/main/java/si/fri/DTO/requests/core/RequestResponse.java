package si.fri.DTO.requests.core;

public abstract class RequestResponse {
    private Integer status;
    private String message;


    public Integer getStatus() {
        return this.status;
    }
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
