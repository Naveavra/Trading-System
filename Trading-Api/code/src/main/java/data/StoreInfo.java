package data;

import domain.store.storeManagement.Store;

public class StoreInfo {
    private int storeId;
    private String name;
    private String description;
    private String img;
    private boolean isActive;
    private int creatorId;
    private double rating;

    public StoreInfo(int creatorId, String name, String storeDescription, String img) {
        this.creatorId = creatorId;
        this.description = storeDescription;
    }

    public StoreInfo(utils.infoRelated.StoreInfo store) {
        this.storeId = store.getStoreId();
        this.name = store.getName();
        this.description = store.getDescription();
        this.isActive = store.getIsActive();
        this.creatorId = store.getCreatorId();
        this.rating = store.getRating();
        this.img = store.getUrl();
    }

    public StoreInfo(Store store) {
        this.storeId = store.getStoreId();
        this.creatorId = store.getCreator();
    }

    public int getStoreId() {
        return storeId;
    }
    public String getStoreName(){return name;}
    public String getImg(){return img;}

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
