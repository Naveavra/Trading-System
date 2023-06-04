package database.dtos;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "inventory")
public class InventoryDto {

    @Id
    private int productId;
    @Id
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
    public InventoryDto(StoreDto storeDto,int productId, String category, int quantity, String name, String description
    , double price, String img){
        this.storeDto = storeDto;
        this.productId = productId;
        this.category = category;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;

    }
    //we insert the category list using tostring so we should check how its actually going to be represented
    //and deal with it when fetching from the db

    public StoreDto getStoreDto(){return storeDto;}

    public int getQuantity(){return this.quantity;}

    public void setQuantity(int quantity){this.quantity = quantity;}

    public String getName(){return this.name;}

    public void setName(){this.name = name;}

    public String getDescription(){return this.description;}

    public void setDescription(String description){this.description = description;}

    public double getPrice(){return this.price;}

    public void setPrice(double price){this.price = price;}

    public String getImg(){return this.img;}

    public void setImg(String img){this.img = img;}

    public void setCategory(List<String> category){this.category = category.toString();}//TODO maybe
    //we should implement it ni the dao

    public int getProductId(){return productId;}

    public void setProductId(int id){this.productId = id;}


}
