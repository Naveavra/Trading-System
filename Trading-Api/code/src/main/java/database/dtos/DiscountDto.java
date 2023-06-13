package database.dtos;
import jakarta.persistence.*;


@Entity
@Table(name = "discounts")
public class DiscountDto {

    @Id
    private int discountId;
    @Id
    private int storeId;
    private String content;
    public DiscountDto(){}
    public DiscountDto(int storeId, int discountId, String content){
        this.storeId =storeId;
        this.discountId = discountId;
        this.content = content;
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
}
