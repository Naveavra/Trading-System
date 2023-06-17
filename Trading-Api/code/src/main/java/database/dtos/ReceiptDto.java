package database.dtos;

import database.DbEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "receiptsProducts")
public class ReceiptDto implements DbEntity {

    @Id
    private int orderId;
    @Id
    private int storeId;
    @Id
    private int productId;
    private int quantity;

    public ReceiptDto(){
    }

    public ReceiptDto(int receiptId, int storeId, int productId, int quantity){
        this.orderId = receiptId;
        this.storeId = storeId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void initialParams() {
    }
}
