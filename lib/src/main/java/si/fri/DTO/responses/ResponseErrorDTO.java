package si.fri.DTO.responses;

public class ResponseErrorDTO extends ResponseAbstract {
    public ResponseErrorDTO(Integer status, String message) {
        super(status, message);
    }
}
