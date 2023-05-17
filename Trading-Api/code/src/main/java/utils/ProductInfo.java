package utils;

import utils.messageRelated.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductInfo {

    public final int storeId;
    public final int id;
    private List<String> categories;
    public String name;
    public String description;
    public int price; //for one product
    public int quantity;
    public double rating;
    public String img;
    public HashMap<Integer, Message> reviews;


    public ProductInfo(int storeId, int productId ,String name , String desc ,int price , int quantity, double rating, HashMap<Integer, Message> reviews,
                       String img){
        this.storeId = storeId;
        this.id = productId;
        this.name =name;
        this.description =desc;
        this.price = price;
        this.quantity =quantity;
        this.rating = rating;
        this.reviews = reviews;
        this.img = img;
    }
    public int getStoreId(){return storeId;}
    public void setCategories(ArrayList<String> cat){
        this.categories = cat;
    }

    public List<String> getCategories() {
        return categories;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public double getRating(){return rating;}
    public HashMap<Integer, Message> getReviews(){return reviews;}
    public String getImg(){return img;}
}
