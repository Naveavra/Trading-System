package utils.orderRelated;


import domain.store.product.Product;
import domain.user.ShoppingCart;
import domain.user.User;
import utils.infoRelated.ProductInfo;

import java.util.HashMap;
import java.util.List;

public class Order {
    private int orderId;
    private Status status;
    private User user;
    private ShoppingCart productsInStores;    //<storeID,<productID, quantity>>
    private HashMap<Integer,HashMap<Integer,Integer>> prices; //storeId,<prodId, price>
    private double totalPrice = 0;
    public Order(int id, User user, ShoppingCart products){
        orderId = id;
        this.user = user;
        status = Status.pending;
        productsInStores = products;
    }
    public synchronized double getTotalPrice(){
        return totalPrice;
    }
    public synchronized double getTotalBasketPrice(int storeID){
        return productsInStores.getBasket(storeID).getTotalPrice();
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
    public synchronized User getUser() {
        return user;
    }
    public synchronized List<ProductInfo> getProductsInStores() {
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
    public synchronized void addProductsToOrder(int storeID,List<ProductInfo> products) throws Exception {
        if(storeID<0){
            throw new Exception("Invalid store id, unable to add products to order.");
        }
        for(ProductInfo product : products)
            productsInStores.changeQuantityInCart(storeID, product, product.quantity);
    }

    /**
     * the name says it all, dont be daft.
     * @param storeID int
     * @param products HashMap<productID,quantity>
     */
    public synchronized void replaceProductsInOrder(int storeID,List<ProductInfo> products) throws Exception{
        addProductsToOrder(storeID, products);
        //this.productsInStores.put(storeID,products);
    }


    public HashMap<Integer, HashMap<Integer,Integer>> getPrices() {
        if(prices == null){
            prices = new HashMap<>();
            for(ProductInfo pI: getShoppingCart().getContent()){
                if(!prices.containsKey(pI.getStoreId())){
                    prices.put(pI.getStoreId(),new HashMap<>());
                }
                prices.get(pI.getStoreId()).put(pI.id,pI.price*pI.quantity);
            }
        }
        return prices;
    }
}
