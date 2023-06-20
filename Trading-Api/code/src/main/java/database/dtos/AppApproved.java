package database.dtos;

import database.DbEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "appApproves")
public class AppApproved implements DbEntity {

    @Id
    private int storeId;
    @Id
    private int fatherId;
    @Id
    private int childId;
    @Id
    private String approverName;
    private boolean approved;

    public AppApproved(){
    }

    public AppApproved(int storeId, int fatherId, int childId, String approverName, boolean approved){
        this.storeId = storeId;
        this.fatherId = fatherId;
        this.childId = childId;
        this.approverName = approverName;
        this.approved = approved;
    }

    @Override
    public void initialParams() {

    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
