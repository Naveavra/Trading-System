package database.dtos;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class AppointmentDto {

    private int storeId;
    @Id
    private int fatherId;
    @Id
    private int childId;


    public AppointmentDto() {
    }
    public AppointmentDto(int storeId, int fatherId, int childId) {
        this.storeId = storeId;
        this.fatherId = fatherId;
        this.childId = childId;
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
}
