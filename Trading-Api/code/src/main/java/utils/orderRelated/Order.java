package utils.orderRelated;


import domain.user.ShoppingCart;

import java.util.HashMap;
import java.util.List;

public class Order {
    private int orderId;
    private Status status;
    private int userId;
    private ShoppingCart productsInStores;    //<storeID,<productID, quantity>>
    private HashMap<Integer,HashMap<Integer,Integer>> prices; //storeId,<prodId, price>
    private double totalPrice = 0;
    public Order(int id, int user_id, ShoppingCart products){
        orderId = id;
        userId = user_id;
        status = Status.pending;
        productsInStores = products;
        prices = new HashMap<>();
    }
    public synchronized double getTotalPrice(){
        return totalPrice;
    }
    public synchronized void setTotalPrice(double price){
        totalPrice = price;
    }
    public synchronized int getOrderId() {
        return orderId;
    }
    public synchronized Status getStatus() {
        return status;
    }
    public synchronized void setStatus(Status stat){
        this.status = stat;
    }
    public synchronized int getUserId() {
        return userId;
    }
    public synchronized HashMap<Integer, HashMap<Integer, Integer>> getProductsInStores() {
        return productsInStores.getContent();
    }

    public synchronized ShoppingCart getShoppingCart(){
        return productsInStores;
    }
    /**
     * This functions' purpose is to add the products,quantity list into the
     global variable this.productsInStores in the storeID entry.
     If the entry doesn't exist it adds a new store entry and the products associated
     with it, otherwise it will add to the quantity of the product if the productID entry
     exists or creates a new productID entry with the specified quantity.
     * @param storeID int
     * @param products HashMap<productID,quantity>
     */
    public synchronized void addProductsToOrder(int storeID,HashMap<Integer, Integer> products) throws Exception {
        if(storeID<0){
            throw new Exception("Invalid store id, unable to add products to order.");
        }
        for(int productId : products.keySet())
            productsInStores.changeQuantityInCart(storeID, productId, products.get(productId));
    }

    /**
     * the name says it all, dont be daft.
     * @param storeID int
     * @param products HashMap<productID,quantity>
     */
    public synchronized void replaceProductsInOrder(int storeID,HashMap<Integer, Integer> products) throws Exception{
        addProductsToOrder(storeID, products);
        //this.productsInStores.put(storeID,products);
    }


    public HashMap<Integer, HashMap<Integer,Integer>> getPrices() {
        return prices;
    }
}
