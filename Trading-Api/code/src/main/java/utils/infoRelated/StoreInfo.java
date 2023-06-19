package utils.infoRelated;

import domain.store.discount.Discount;
import domain.store.purchase.PurchasePolicy;
import domain.store.storeManagement.Bid;
import org.json.JSONObject;

import java.util.List;

public class StoreInfo extends Information{

    private int storeId;
    private String storeName;
    private String description;
    private boolean isActive;
    private int creatorId;
    private double rating;
    private String imgUrl;
    private List<Discount> discounts;
    private List<PurchasePolicy> purchases;

    private List<Bid> bids; // only open bids
    public StoreInfo(int storeId, String name, String description, boolean isActive, int creatorId, double rating, String url,
                     List<Bid> bids , List<Discount> discounts, List<PurchasePolicy> purchases) {
        this.storeId = storeId;
        storeName = name;
        this.description = description;
        this.isActive = isActive;
        this.creatorId = creatorId;
        this.rating = rating;
        imgUrl = url;
        this.bids = bids;
        this.discounts = discounts;
        this.purchases = purchases;
    }

    public int getStoreId(){
        return storeId;
    }
    public String getName(){return storeName;}

    public String getDescription(){
        return description;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public int getCreatorId(){
        return creatorId;
    }

    public double getRating(){
        return rating;
    }
    public String getUrl(){return imgUrl;}

    public List<Bid> getBids(){return bids;}
    public List<Discount> getDiscounts(){
        return discounts;
    }
    public List<PurchasePolicy> getPurchases(){
        return purchases;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("storeId", getStoreId());
        json.put("name", getName());
        json.put("description", getDescription());
        json.put("isActive", getIsActive());
        json.put("creatorId", getCreatorId());
        json.put("rating", getRating());
        json.put("img", getUrl());
        json.put("bids", infosToJson(bids));
        json.put("discounts",infosToJson(getDiscounts()));
        json.put("purchasePolicies",infosToJson(getPurchases()));
        return json;
    }
}
