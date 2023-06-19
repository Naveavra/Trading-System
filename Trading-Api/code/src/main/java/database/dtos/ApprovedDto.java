package database.dtos;

import database.DbEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "approves")
public class ApprovedDto implements DbEntity {

    @Id
    public int bidId;

    @Id
    public String manager;

    public ApprovedDto(){
    }

    public ApprovedDto(int bidId, String manager){
        this.bidId = bidId;
        this.manager = manager;
    }


    @Override
    public void initialParams() {
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
