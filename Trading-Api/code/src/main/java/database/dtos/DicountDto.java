package database.dtos;
import jakarta.persistence.*;


@Entity
@Table(name = "discounts")
public class DicountDto {

    @Id
    private int discountId;
    @Id
    @ManyToOne
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "storeId"), referencedColumnName = "storeId")
    private StoreDto storeDto;
    private String content;
    public DicountDto(){}
    public DicountDto(StoreDto storeDto, int discountId, String content){
        this.storeDto = storeDto;
        this.discountId = discountId;
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

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }
}
