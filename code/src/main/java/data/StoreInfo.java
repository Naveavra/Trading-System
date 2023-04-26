package data;

import domain.store.storeManagement.Store;

public class StoreInfo {
    private int storeId;
    private String description;
    private boolean isActive;
    private int creatorId;
    private double rating;

    public StoreInfo(int creatorId, String storeDescription) {
        this.creatorId = creatorId;
        this.description = storeDescription;
    }

    public StoreInfo(utils.StoreInfo store) {
        this.storeId = store.getStoreId();
        this.description = store.getDescription();
        this.isActive = store.getIsActive();
        this.creatorId = store.getCreatorId();
        this.rating = store.getRating();
    }

    public StoreInfo(Store store) {
        this.storeId = store.getStoreId();
        this.creatorId = store.getCreatorId();
    }

    public int getStoreId() {
        return storeId;
    }

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
