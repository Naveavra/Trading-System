package domain.store.storeManagement;

import database.DbEntity;
import database.daos.Dao;
import database.daos.StoreDao;
import database.daos.SubscriberDao;
import database.dtos.ApproverDto;
import domain.store.product.Product;
import domain.user.Member;
import jakarta.persistence.*;
import org.hibernate.Session;
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

    public Bid(){
    }
    public Bid(int id,Member user, int storeId, Product p, double offer, int quantity, ArrayList<String> approvers,
               Session session) throws Exception {
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
            Dao.save(new ApproverDto(bidId, storeId, manager, false), session);
        }
        counterOffer = "";
        StoreDao.saveBid(this, session);
    }
    public void approveBid(String userName, Session session) throws Exception {
        if(approvers.get(userName)){
            throw new Exception(userName + " already approved this bid");
        }
        approvers.put(userName, true);
        Dao.save(new ApproverDto(bidId, storeId, userName, approvers.get(userName)), session);
        if(allApproved())
            this.approved = status.Approved;
    }
    public boolean declineBid() throws Exception {
        if(isApproved()){
            //throw new Exception("Cannot decline a bid that was already approved.");
            return false;
        }
        approved = status.Declined;
        return true;
    }
    public void clientAcceptCounter() {
        approved = status.Pending;
    }
    public void counterBid(double offer,String userName, Session session) throws Exception {
        if(Objects.equals(counterOffer, "")) {
            counterOffer = userName;
            this.offer = offer;
            this.approved = status.Counter;
            approvers.replaceAll((n, v) -> false);
            for(String name : approvers.keySet())
                Dao.save(new ApproverDto(bidId, storeId, name, false), session);
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
    public void setStatus(status status, Session session) throws Exception{
        this.approved = status;
        StoreDao.saveBid(this, session);
    }

    public String getBidTime() {
        return bidTime;
    }

    public boolean containsInApprove(String name) {
        for(String userName : approvers.keySet())
            if(userName.equals(name))
                return true;
        return false;
    }

    public void removeApprover(String name, Session session) throws Exception{
        approvers.remove(name);
        Dao.removeIf("ApproverDto", String.format("storeId = %d AND manager = '%s' ", storeId, name), session);
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
    public void initialParams()throws Exception {
        getProductFromDb();
        getUserFromDb();
        getApproversFromDb();

    }

    private void getProductFromDb()throws Exception{
        if(product == null)
            product = StoreDao.getProduct(storeId, productId);
    }

    private void getUserFromDb() throws Exception{
        if(user == null)
            user = SubscriberDao.getMember(userId);
    }

    private void getApproversFromDb() throws Exception{
        if(approvers == null){
            approvers = new HashMap<>();
            List<? extends  DbEntity> approversDto = Dao.getByParamList(ApproverDto.class, "ApproverDto",
                    String.format("storeId = %d AND bidId = %d", storeId, bidId));
            for(ApproverDto approverDto : (List<ApproverDto>) approversDto)
                approvers.put(approverDto.getManager(), approverDto.isApproved());
        }
    }

}
