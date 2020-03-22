package si.fri.DTO.requests.core;

import si.fri.DTO.requests.AWSNewBucket;

public class AWSRequestResponse extends RequestResponse {
    AWSNewBucket data;

    public AWSNewBucket getData() {
        return data;
    }

    public void setData(AWSNewBucket data) {
        this.data = data;
    }
}
