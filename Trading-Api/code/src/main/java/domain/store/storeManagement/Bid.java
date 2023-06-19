package domain.store.storeManagement;

import database.DbEntity;
import database.daos.Dao;
import database.daos.StoreDao;
import database.dtos.ApprovedDto;
import domain.store.product.Product;
import domain.user.Member;
import jakarta.persistence.*;
import org.json.JSONObject;
import utils.infoRelated.Information;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "bids")
public class Bid extends Information implements DbEntity {


    public enum status {Declined,Approved,Pending, Counter}
    @Id
    public int bidId;
    @Id
    public int storeId;
    @Transient
    public Product product;
    @Id
    public int productId;
    public double offer; //how much user has offered for Product.
    public int quantity; //how much does he want to buy.

    public status approved;
    public String bidTime;

    @Transient
    public Member user;

    public int userId;
    @Transient
    public ArrayList<String> approveCount;
    @Transient
    public ArrayList<String> approvers;
    public String counterOffer;
    public Bid(int id,Member user, int storeId, Product p, double offer, int quantity, ArrayList<String> approvers){
        this.bidId = id;
        this.offer = offer;
        this.quantity = quantity;
        this.storeId = storeId;
        this.product = p;
        if(p != null)
            productId = p.getID();
        this.user = user;
        if(user != null)
            userId = user.getId();
        this.approved = status.Pending;
        this.bidTime = LocalDateTime.now().toString();
        this.approvers = approvers;
        approveCount = new ArrayList<>();
        counterOffer = "";
        StoreDao.saveBid(this);
    }
    public void approveBid(String userName) throws Exception {
        if(!approvers.contains(userName)){
            throw new Exception("User cannot approve this bid, "+userName);
        }
        if(approveCount .contains(userName)){
            throw new Exception("User cannot approve the same bid twice");
        }
        approveCount.add(userName);
        Dao.save(new ApprovedDto(bidId, userName));
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
        if(Objects.equals(counterOffer, "")) {
            counterOffer = userName;
            this.offer = offer;
            this.approved = status.Counter;
            approveCount = new ArrayList<>();
            return;
        }
        throw new Exception("Cannot counter bid twice");

    }

    public void editBid(double offer,int quantity){
        counterOffer ="";
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
    public int getProductId(){return productId;}
    public int getQuantity(){
        return quantity;
    }

    public double getOffer() {
        return offer;
    }
    public int getStoreId(){
        return storeId;
    }

    public int getBidId(){return bidId;}

    public boolean isPending() {
        return approved == status.Pending;
    }
    public void setApprovers(ArrayList<String> approversNames){
        this.approvers = approversNames;
    }

    public status getState(){return this.approved;}

    public String getBidTime() {
        return bidTime;
    }
    public ArrayList<String> getApprovers(){return this.approvers;}

    public String getCounterOffer() {
        return counterOffer;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bidId", getBidId());
        jsonObject.put("storeId", getStoreId());
        jsonObject.put("offer", getOffer());
        jsonObject.put("product", product.getProductInfo().toJson());
        jsonObject.put("quantity", getQuantity());
        jsonObject.put("user", getUser().getId());
        jsonObject.put("state", getState().toString());
        jsonObject.put("time", getBidTime());
        jsonObject.put("approvers", getApprovers());
        jsonObject.put("count", getCounterOffer());
        return jsonObject;
    }


    @Override
    public void initialParams() {

    }

}
