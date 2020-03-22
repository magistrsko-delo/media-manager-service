package si.fri.DTO.requests.core;

import si.fri.DTO.requests.NewMediaResponseData;

public class NewMediaResponse extends RequestResponse {
    NewMediaResponseData data;

    public NewMediaResponseData getData() {
        return data;
    }

    public void setData(NewMediaResponseData data) {
        this.data = data;
    }
}
