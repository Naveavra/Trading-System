package database.dtos;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryDto {


    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "storeId", foreignKey = @ForeignKey(name = "storeId"), referencedColumnName = "storeId"),
            @JoinColumn(name = "productId", foreignKey = @ForeignKey(name = "productId"), referencedColumnName = "productId")
    })
    private InventoryDto inventoryDto;

    @Id
    private String categoryName;

    public CategoryDto(){
    }

    public CategoryDto(InventoryDto inventoryDto, String categoryName){
        this.inventoryDto = inventoryDto;
        this.categoryName = categoryName;
    }

    public InventoryDto getInventoryDto(){
        return inventoryDto;
    }

    public void setInventoryDto(InventoryDto inventoryDto){
        this.inventoryDto = inventoryDto;
    }

    public String getCategoryName(){
        return categoryName;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

}
