package utils.infoRelated;

import domain.store.product.Product;
import org.json.JSONObject;
import utils.messageRelated.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductInfo extends Information{

    public final int storeId;
    public int id;
    private List<String> categories;
    public String name;
    public String description;
    public int price; //for one product
    public int quantity;
    public double rating;
    public String img;
    public HashMap<Integer, Message> reviews;


    public ProductInfo(int storeId, int productId, List<String> categories, String name, String desc, int price, int quantity, double rating, HashMap<Integer, Message> reviews,
                       String img){
        this.storeId = storeId;
        this.id = productId;
        this.categories = new ArrayList<>();
        this.categories.addAll(categories);
        this.name =name;
        this.description =desc;
        this.price = price;
        this.quantity =quantity;
        this.rating = rating;
        this.reviews = reviews;
        this.img = img;
    }

    public ProductInfo(int storeId, Product product, int quantity){
        this.storeId = storeId;
        this.id = product.getID();
        this.categories = new ArrayList<>();
        this.categories.addAll(product.getCategories());
        this.name =product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity =quantity;
        this.rating = product.getRating();
        this.reviews = new HashMap<>();
        this.img = product.getImgUrl();
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

    @Override
    public  JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("storeId", getId());
        json.put("productId", getId());
        json.put("name", getName());
        json.put("description", getDescription());
        json.put("price", getPrice());
        json.put("quantity", getQuantity());
        json.put("categories", getCategories());
        json.put("rating", getRating());
        json.put("reviews", hashMapToJson(getReviews(), "messageId", "review"));
        json.put("img", getImg());
        return json;
    }
}
