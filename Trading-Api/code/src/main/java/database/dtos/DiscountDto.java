package database.dtos;
import database.DbEntity;
import jakarta.persistence.*;


@Entity
@Table(name = "discounts")
public class DiscountDto implements DbEntity {

    @Id
    private int discountId;
    @Id
    private int storeId;
    private String content;
    private String description;
    public DiscountDto(){}
    public DiscountDto(int storeId, int discountId, String content, String description){
        this.storeId =storeId;
        this.discountId = discountId;
        this.content = content;
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
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
