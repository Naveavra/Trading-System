package utils.infoRelated;

import domain.user.ShoppingCart;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class OrderInfo extends Information{

    private int orderId;
    private int userId;
    private ShoppingCart productsInStores;
    private double totalPrice;

    public OrderInfo(int orderId, int userId, ShoppingCart productsInStores, double totalPrice){

        this.orderId = orderId;
        this.userId = userId;
        this.productsInStores = productsInStores;
        this.totalPrice = totalPrice;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<ProductInfo> getProductsInStores(){
        return productsInStores.getContent();
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("orderId", getOrderId());
        json.put("userId", getUserId());
        json.put("totalPrice", getTotalPrice());
        json.put("productsInStores", infosToJson(productsInStores.getContent()));
        return json;
    }
}
