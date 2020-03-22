package si.fri.DTO.requests;

import java.util.Date;

public class AWSNewBucket {
    String name;
    String owner;
    Date creationDate;

    public String getName() {
        return name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
