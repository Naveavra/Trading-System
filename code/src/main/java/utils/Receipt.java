package utils;

import java.util.HashMap;

public class Receipt {

    private int userId;
    private transient int orderId;
    private HashMap<Integer, HashMap<Integer, Integer>> products; // a hashmap from storeId to hashmap from productId to quantity
    private int totalPrice;

    public Receipt(int userId,int orderId,HashMap<Integer, HashMap<Integer, Integer>> products,int totalPrice){
        this.userId = userId;
        this.orderId = orderId;
        this.products = products;
        this.totalPrice = totalPrice;
    }
    public int getOrderId(){
        return orderId;
    }
}
