package domain.store.storeManagement;

import domain.store.product.Product;
import domain.user.Member;

import java.time.LocalDateTime;

import static domain.store.storeManagement.Bid.status.*;

public class Bid {
    public enum status {Declined,Approved,Pending};
    public double offer; //how much user has offered for Product.
    public int quantity; //how much does he want to buy.
    public Product product;
    public status approved;
    public LocalDateTime bidTime;
    public Member user;
    public Bid(Member user,Product p,double offer,int quantity){
        this.offer = offer;
        this.quantity = quantity;
        this.product = p;
        this.approved = Pending;
        this.bidTime = LocalDateTime.now();
    };
    public void approveBid(){
        this.approved = Approved;
    }
    public void declineBid() throws Exception {
        if(isApproved()){
            throw new Exception("Cannot decline a bid that was already approved.");
        }
        approved = Declined;
    }
    public boolean isApproved(){
        return approved == Approved;
    }
    public Member getUser(){
        return user;
    }
    public Product getProduct() {
        return product;
    }
    public int getQuantity(){
        return quantity;
    }

    public double getOffer() {
        return offer;
    }


}
