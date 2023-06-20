package database.dtos;

import database.DbEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bidApproves")
public class ApproverDto implements DbEntity {
    @Id
    private int bidId;
    @Id
    private int storeId;
    @Id
    private String manager;

    private boolean approved;

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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
