package database.dtos;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class InventoryDto {

    @Id
    private int productId;
    @ManyToOne
    @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "storeId"), referencedColumnName = "storeId")
    private StoreDto storeDto;
    private String category;
    private int quantity;
    private String name;
    private String description;
    private double price;
    private String img;

    public InventoryDto(){};
    public InventoryDto(StoreDto storeDto, String category, int quantity, String name, String description
    , double price, String img){
        this.storeDto = storeDto;
        this.category = category;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;
    }

    public StoreDto getStoreDto(){return storeDto;}

    public


}
