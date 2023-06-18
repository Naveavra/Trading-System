package domain.store.storeManagement;

import data.ProductInfo;
import domain.store.product.Product;
import domain.user.Member;
import org.json.JSONObject;
import utils.infoRelated.Information;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Bid extends Information {




    public enum status {Declined,Approved,Pending, Counter};
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
        this.approved = status.Pending;
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
            this.approved = status.Approved;
    }
    public void declineBid() throws Exception {
        if(isApproved()){
            throw new Exception("Cannot decline a bid that was already approved.");
        }
        approved = status.Declined;
    }
    public void clientAcceptCounter() {
        approved = status.Pending;
    }
    public void counterBid(double offer,String userName) throws Exception {
        if(Objects.equals(counter, "")) {
            counter = userName;
            this.offer = offer;
            this.approved = status.Counter;
            approveCount = new ArrayList<>();
            return;
        }
        throw new Exception("Cannot counter bid twice");

    }

    public void editBid(double offer,int quantity){
        counter ="";
        this.offer = offer;
        this.quantity = quantity;
        this.approved = status.Pending;
        approveCount = new ArrayList<>();
    }
    public boolean isApproved(){
        return approved == status.Approved;
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

    public int getBidId(){return bidId;}

    public boolean isPending() {
        return approved == status.Pending;
    }
    public void setApprovers(ArrayList<String> approversNames){
        this.approvers = approvers;
    }

    public status getState(){return this.approved;}

    public LocalDateTime getBidTime() {
        return bidTime;
    }
    public ArrayList<String> getApprovers(){return this.approvers;}

    public String getCounter() {
        return counter;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bidId", getBidId());
        jsonObject.put("offer", getOffer());
        jsonObject.put("product", product.getProductInfo().toJson());
        jsonObject.put("quantity", getQuantity());
        jsonObject.put("user", getUser().getId());
        jsonObject.put("state", getState().toString());
        jsonObject.put("time", getBidTime().toString());
        jsonObject.put("approvers", getApprovers());
        jsonObject.put("count", getCounter());
        return jsonObject;
    }

}
