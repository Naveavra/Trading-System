package service;

import domain.store.order.Order;
import domain.store.order.OrderController;
import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;
import domain.user.Member;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import jdk.jshell.spi.ExecutionControl;
import utils.Message;

public class MarketController {

    StoreController storectrl;
    //ProductController productctrl;
    OrderController orderctrl;
    UserController userCtrl;
    Gson gson;
    public MarketController()
    {
        storectrl = new StoreController();
        orderctrl = new OrderController();
        userCtrl = new UserController();
        gson = new Gson();
    }

    public String purchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingCart, int userId,int totalPrice)
    {
        Order order = orderctrl.createNewOrder(userId,shoppingCart);
        order.setTotalPrice(totalPrice);
        storectrl.purchaseProducts(shoppingCart);
        return gson.toJson(order);
        //TODO SOMETHING WITH ORDER
    }

    /**
     * @param userID creator id
     * @param description store description
     */
    public Store openStore(int userID, String description) throws Exception
    {

        Store store = storectrl.openStore(description, userID);
       return store;


    }

    public String getProductName(int storeId ,int  productId) throws Exception {
        return storectrl.getProductName(storeId , productId);
    }

    public ArrayList<String> checkMessages(int storeID) throws Exception {

        return storectrl.checkMessages(storeID);
    }
    public void giveFeedback(int storeID, int messageID, String feedback ) throws Exception
    {
        storectrl.giveFeedback(storeID, messageID, feedback);
    }




    public void addReviewToStore(int storeId, int orderId, Message m) throws Exception {
        Store store = this.storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            store.addReview(orderId, m);
        }
        else
        {
            throw new Exception("store doesn't not exist or is doesn't active");
        }
    }

    public void writeReviewForProduct(Message m) throws Exception{
        Store store = storectrl.getStore(m.getStoreId());
        if (store != null && store.isActive())
        {
            store.addProductReview(m);
        }

    }

    public String getProductInformation(int storeId, int producId) throws Exception {
        Store store = storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            return store.getProductInformation(producId);
        }
        throw new Exception("cant get product information");
    }

    public String getStoreInformation(int storeId) throws Exception {
        Store store = storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            Gson gson = new Gson();
            return gson.toJson(store);
        }
        throw new Exception("can not show store information");
    }

    public String getStoreDescription(int storeId) throws Exception{
        Store store = storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            return store.getStoreDescription();
        }
        throw new Exception("can not show store information");
    }
    public void setStoreDescription(int storeId,String des) throws Exception{
        Store store = storectrl.getStore(storeId);
        if (store != null )
        {
             store.setStoreDescription(des);
        }
        throw new Exception("store does not exist");
    }
    public void setStorePurchasePolicy(int storeId,String policy) throws Exception{
        Store store = storectrl.getStore(storeId);
        if (store != null )
        {
            store.setStorePolicy(policy);
        }
        throw new Exception("store does not exist");
    }



    public int caclulatePrice(HashMap<Integer, HashMap<Integer, Integer>> shoppingCart) {
        int totalprice = 0;
        for (Integer storeid : shoppingCart.keySet())
        {
            Store store = storectrl.getStore(storeid);
            try {
                totalprice += store.caclulatePrice(shoppingCart.get(storeid));
            } catch (Exception e) {
                return  -1;
            }
        }
        return totalprice;
    }

    public String getStoreProducts(int storeId) throws Exception {
        Store s = storectrl.getStore(storeId);
        if(s==null){
            throw new Exception("store not available");
        }
        if(!s.isActive()){
            throw new Exception("store not available");
        }
        return s.getProducts();
    }

    public void sendQuestion(int storeId, Message message) throws Exception {
        Store s =storectrl.getStore(storeId);
        if(s != null){
            s.addQuestion(message);
        }
        throw new Exception("store doesn't exist");
    }

    public void setStoreDiscountPolicy(int storeId, String policy) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("miki implement please");
    }
}
