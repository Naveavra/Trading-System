package utils;

public class StoreInfo {

    private int storeId;
    private String storeName;
    private String description;
    private boolean isActive;
    private int creatorId;
    private double rating;
    private String imgUrl;
    public StoreInfo(int storeId, String name, String description, boolean isActive, int creatorId, double rating, String url) {
        this.storeId = storeId;
        storeName = name;
        this.description = description;
        this.isActive = isActive;
        this.creatorId = creatorId;
        this.rating = rating;
        imgUrl = url;
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

}
