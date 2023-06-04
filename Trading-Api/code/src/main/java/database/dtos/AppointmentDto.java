package database.dtos;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class AppointmentDto {

    @Id
    @ManyToOne
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId")
    private StoreDto storeDto;
    @Id
    private int fatherId;
    @Id
    private int childId;


    public AppointmentDto() {
    }
    public AppointmentDto(StoreDto storeDto, int fatherId, int childId) {
        this.storeDto = storeDto;
        this.fatherId = fatherId;
        this.childId = childId;
    }

    public StoreDto getStoreDto() {
        return storeDto;
    }

    public void setStoreDto(StoreDto storeDto) {
        this.storeDto = storeDto;
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
