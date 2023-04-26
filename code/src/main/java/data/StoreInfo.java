package data;

import domain.store.storeManagement.Store;

public class StoreInfo {
    private int storeId;
    private int creatorId;
    private String storeDescription;

    public StoreInfo(int creatorId, String storeDescription) {
        this.creatorId = creatorId;
        this.storeDescription = storeDescription;
    }

    public StoreInfo(Store store) {
        this.creatorId = store.getCreatorId();
        this.storeDescription = store.getStoreDescription();
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

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }
}
