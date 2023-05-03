package utils;

public class StoreInfo {

    private int storeId;
    private String description;
    private boolean isActive;
    private int creatorId;
    private double rating;

    public StoreInfo(int storeId, String description, boolean isActive, int creatorId, double rating) {
        this.storeId = storeId;
        this.description = description;
        this.isActive = isActive;
        this.creatorId = creatorId;
        this.rating = rating;
    }

    public int getStoreId(){
        return storeId;
    }

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

}
