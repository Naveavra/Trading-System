package database.dtos;

import jakarta.persistence.*;

@Entity @Table(name = "carts")
public class CartDto {

    @ManyToOne
    @Transient
    @Id
    private MemberDto memberDto;
    @Id
    private int storeId;
    @Id
    private int productId;
    private int quantity;

    public CartDto(){
    }

    public CartDto(MemberDto memberDto, int storeId, int productId, int quantity){
        this.memberDto = memberDto;
        this.storeId = storeId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MemberDto getMemberDto() {
        return memberDto;
    }

    public void setMemberDto(MemberDto memberDto) {
        this.memberDto = memberDto;
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
}
