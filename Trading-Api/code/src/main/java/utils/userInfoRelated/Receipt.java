package utils.userInfoRelated;

import java.util.HashMap;

public class Receipt {

    private int userId;
    private transient int orderId;
    private HashMap<Integer, HashMap<Integer, Integer>> products; // a hashmap from storeId to hashmap from productId to quantity
    private double totalPrice;

    public Receipt(int userId,int orderId,HashMap<Integer, HashMap<Integer, Integer>> products,double totalPrice){
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
        return products;
    }
}
