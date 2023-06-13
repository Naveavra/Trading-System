package database.dtos;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryDto {


    @Id
    private int storeId;
    @Id
    private int productId;

    @Id
    private String categoryName;

    public CategoryDto(){
    }

    public CategoryDto(int storeId, int productId, String categoryName){
        this.storeId = storeId;
        this.productId = productId;
        this.categoryName = categoryName;
    }

    public String getCategoryName(){
        return categoryName;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
