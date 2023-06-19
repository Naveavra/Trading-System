package domain.store.storeManagement;

import database.DbEntity;
import database.daos.Dao;
import database.daos.StoreDao;
import database.dtos.ApproverDto;
import domain.store.product.Product;
import domain.user.Member;
import jakarta.persistence.*;
import org.json.JSONObject;
import utils.infoRelated.Information;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "bids")
public class Bid extends Information implements DbEntity{

    public enum status {Declined,Approved,Pending, Counter, Completed}
    @Id
    private int bidId;
    @Id
    private int storeId;
    @Transient
    private Product product;
    @Id
    private int productId;
    @Transient
    private Member user;
    private int userId;
    private double offer; //how much user has offered for Product.
    private int quantity; //how much does he want to buy.
    private status approved;
    private String bidTime;
    @Transient
    private HashMap<String, Boolean> approvers;
    private String counterOffer;
    public Bid(int id,Member user, int storeId, Product p, double offer, int quantity, ArrayList<String> approvers){
        this.bidId = id;
        this.offer = offer;
        this.quantity = quantity;
        this.storeId = storeId;
        this.product = p;
        productId = p.getID();
        this.user = user;
        userId = user.getId();
        this.approved = status.Pending;
        this.bidTime = LocalDate.now().toString();
        this.approvers = new HashMap<>();
        for(String manager : approvers) {
            this.approvers.put(manager, false);
            Dao.save(new ApproverDto(bidId, storeId, manager, false));
        }
        counterOffer = "";
        StoreDao.saveBid(this);
    }
    public void approveBid(String userName) throws Exception {
        if(approvers.get(userName)){
            throw new Exception(userName + " already approved this bid");
        }
        approvers.put(userName, true);
        Dao.save(new ApproverDto(bidId, storeId, userName, approvers.get(userName)));
        if(allApproved())
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
            approvers.replaceAll((n, v) -> false);
            for(String name : approvers.keySet())
                Dao.save(new ApproverDto(bidId, storeId, name, false));
            return;
        }
        throw new Exception("Cannot counter bid twice");

    }

    public void editBid(double offer,int quantity){
        counterOffer ="";
        this.offer = offer;
        this.quantity = quantity;
        this.approved = status.Pending;
        approvers.replaceAll((n, v) -> false);
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
        approvers.clear();
        approvers = new HashMap<>();
        for(String name : approversNames)
            approvers.put(name, false);
    }

    public status getState(){return this.approved;}
    public void setStatus(status status) {
        this.approved = status;
        StoreDao.saveBid(this);
    }

    public String getBidTime() {
        return bidTime;
    }
    public ArrayList<String> getApprovers(){return new ArrayList<>(this.approvers.keySet());}
    public List<String> getApproved(){
        List<String> ans = new ArrayList<>();
        for(String name : approvers.keySet())
            if(approvers.get(name))
                ans.add(name);
        return ans;
    }

    public List<String> getNotApproved(){
        List<String> ans = new ArrayList<>();
        for(String name : approvers.keySet())
            if(!approvers.get(name))
                ans.add(name);
        return ans;
    }

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
        jsonObject.put("userId", getUser().getId());
        jsonObject.put("state", getState().toString());
        jsonObject.put("time", getBidTime());
        jsonObject.put("approved", getApproved());
        jsonObject.put("notApproved", getNotApproved());
        jsonObject.put("count", getCounterOffer());
        return jsonObject;
    }


    public boolean allApproved(){
        boolean ans = true;
        for(boolean approved : approvers.values())
            ans = ans && approved;
        return ans;
    }

    @Override
    public void initialParams() {

    }

}
