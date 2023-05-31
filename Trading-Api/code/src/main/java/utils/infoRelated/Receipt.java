package utils.infoRelated;

import domain.user.Basket;
import domain.user.ShoppingCart;
import org.json.JSONObject;

import java.util.List;

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

    public ShoppingCart getCart(){
        return products;
    }

    public List<Basket> getContent(){return products.getBaskets();}

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("orderId", orderId);
        json.put("totalPrice", totalPrice);
        json.put("products", infosToJson(products.getContent()));
        return json;
    }

}
