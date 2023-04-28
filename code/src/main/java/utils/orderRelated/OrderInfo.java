package utils.orderRelated;

import java.util.HashMap;

public class OrderInfo {

    private int orderId;
    private int userId;
    private HashMap<Integer, HashMap<Integer,Integer>> productsInStores;
    private int totalPrice;

    public OrderInfo(int orderId, int userId, HashMap<Integer, HashMap<Integer,Integer>> productsInStores, int totalPrice){

        this.orderId = orderId;
        this.userId = userId;
        this.productsInStores = productsInStores;
        this.totalPrice = totalPrice;
    }


    public HashMap<Integer, HashMap<Integer,Integer>> getProductsInStores(){
        return productsInStores;
    }

    public int getOrderId() {
        return orderId;
    }
}
