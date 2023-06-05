package database.dtos;
import jakarta.persistence.*;

@Entity
@Table(name = "constraints")
public class ConstraintDto {

    @Id
    private int constraintId;
    @Id
    @ManyToOne
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "storeId"), referencedColumnName = "storeId")
    private StoreDto storeDto;
    private String content;
    public ConstraintDto(){}
    public ConstraintDto(StoreDto storeDto, int constraintId, String content){
        this.storeDto = storeDto;
        this.constraintId = constraintId;
        this.content = content;
    }

    public void setStoreDto(StoreDto storeDto) {
        this.storeDto = storeDto;
    }
    public StoreDto getStoreDto() {
        return storeDto;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getConstraintId() {
        return constraintId;
    }

    public void setDiscountId(int constraintId) {
        this.constraintId = constraintId;
    }
}
