package utils.infoRelated;

import domain.user.ShoppingCart;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receipt extends Information{

    private int userId;
    private transient int orderId;
    private ShoppingCart products; // a hashmap from storeId to hashmap from productId to quantity
    private double totalPrice;

    public Receipt(int userId,int orderId,ShoppingCart products,double totalPrice){
        this.userId = userId;
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
    }
    public int getOrderId(){
        return orderId;
    }

    public double getTotalPrice(){return totalPrice;}

    public HashMap<Integer, HashMap<Integer, Integer>> getProducts(){
        return products.getContent();
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("orderId", orderId);
        json.put("totalPrice", totalPrice);
        json.put("products", products.toJson());
        return json;
    }

}
