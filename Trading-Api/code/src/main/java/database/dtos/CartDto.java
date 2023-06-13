package database.dtos;

import jakarta.persistence.*;

@Entity @Table(name = "carts")
public class CartDto {

    @Id
    private int memberId;
    @Id
    private int storeId;
    @Id
    private int productId;
    private int quantity;

    public CartDto(){
    }

    public CartDto(int memberId, int storeId, int productId, int quantity){
        this.memberId = memberId;
        this.storeId = storeId;
        this.productId = productId;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
