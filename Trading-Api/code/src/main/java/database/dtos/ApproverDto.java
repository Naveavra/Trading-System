package database.dtos;

import database.DbEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "approves")
public class ApproverDto implements DbEntity {

    @Id
    public int bidId;

    @Id
    public int storeId;

    @Id
    public String manager;

    public boolean approved;

    public ApproverDto(){
    }

    public ApproverDto(int bidId, int storeId, String manager, boolean approved){
        this.bidId = bidId;
        this.storeId = storeId;
        this.manager = manager;
        this.approved = approved;
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
