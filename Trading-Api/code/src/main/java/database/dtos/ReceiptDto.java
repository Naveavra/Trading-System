package database.dtos;

import jakarta.persistence.*;

@Entity
@Table(name = "receipts")
public class ReceiptDto {

    @ManyToOne
    @Transient
    @Id
    private UserHistoryDto userHistoryDto;
    @Id
    private int storeId;
    @Id
    private int productId;
    private int quantity;

    public ReceiptDto(){
    }

    public ReceiptDto(UserHistoryDto userHistoryDto, int storeId, int productId, int quantity){
        this.userHistoryDto = userHistoryDto;
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

    public UserHistoryDto getUserHistoryDto() {
        return userHistoryDto;
    }

    public void setUserHistoryDto(UserHistoryDto userHistoryDto) {
        this.userHistoryDto = userHistoryDto;
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
}
