package domain.store.storeManagement;

import domain.store.product.Product;
import domain.user.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static domain.store.storeManagement.Bid.status.*;

public class Bid {

    public enum status {Declined,Approved,Pending};
    public int bidId;
    public double offer; //how much user has offered for Product.
    public int quantity; //how much does he want to buy.
    public Product product;
    public status approved;
    public LocalDateTime bidTime;
    public Member user;
    public ArrayList<String> approveCount;
    public ArrayList<String> approvers;
    public String counter;
    public Bid(int id,Member user, Product p, double offer, int quantity, ArrayList<String> approvers){
        this.bidId = id;
        this.offer = offer;
        this.quantity = quantity;
        this.product = p;
        this.user = user;
        this.approved = Pending;
        this.bidTime = LocalDateTime.now();
        this.approvers = approvers;
        approveCount = new ArrayList<>();
        counter = "";
    };
    public void approveBid(String userName) throws Exception {
        if(!approvers.contains(userName)){
            throw new Exception("User cannot approve this bid, "+userName);
        }
        if(approveCount .contains(userName)){
            throw new Exception("User cannot approve the same bid twice");
        }
        approveCount.add(userName);
        if(approveCount.size() == approvers.size())
            this.approved = Approved;
    }
    public void declineBid() throws Exception {
        if(isApproved()){
            throw new Exception("Cannot decline a bid that was already approved.");
        }
        approved = Declined;
    }
    public void counterBid(double offer,String userName) throws Exception {
        if(Objects.equals(counter, "")) {
            counter = userName;
            this.offer = offer;
            this.approved = Pending;
            approveCount = new ArrayList<>();
            return;
        }
        throw new Exception("Cannot counter bid twice");

    }

    public void editBid(double offer,int quantity){
        counter ="";
        this.offer = offer;
        this.quantity = quantity;
        this.approved = Pending;
        approveCount = new ArrayList<>();
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

    public boolean isPending() {
        return approved == Pending;
    }
    public void setApprovers(ArrayList<String> approversNames){
        this.approvers = approvers;
    }

}
