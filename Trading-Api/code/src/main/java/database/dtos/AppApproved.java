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
}
