package database.dtos;
import database.DbEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "constraints")
public class ConstraintDto implements DbEntity {

    @Id
    private int constraintId;
    @Id
    private int storeId;
    private String content;
    private String description;
    public ConstraintDto(){}
    public ConstraintDto(int storeId, int constraintId, String content, String description){
        this.storeId = storeId;
        this.constraintId = constraintId;
        this.content = content;
        this.description = description;
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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public void initialParams() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
