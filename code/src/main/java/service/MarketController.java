package service;

import utils.Order;
import domain.store.order.OrderController;
import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import jdk.jshell.spi.ExecutionControl;
import utils.Message;
import utils.Receipt;

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

    public Receipt purchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingCart, int userId,int totalPrice)
    {
        Order order = orderctrl.createNewOrder(userId,shoppingCart);
        order.setTotalPrice(totalPrice);
        storectrl.purchaseProducts(shoppingCart);
        Receipt receipt = new Receipt(userId, order.getOrderId(), shoppingCart, totalPrice);
        return receipt;
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

    public void addProduct(int storeId, String name, String description, int price, int quantity, List<String> categories) throws Exception{
        int id = storectrl.addProduct(storeId,name,description,price,quantity);
        if(id == -1){
            throw new Exception("Something went wrong in adding product");
        }
        storectrl.addToCategory(storeId,id,categories);
    }
    public String getProductInformation(int storeId, int producId) throws Exception {
        Store store = storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            return store.getProductInformation(producId);
        }
        else {
            throw new Exception("cant get product information");
        }
    }

    public String getStoreInformation(int storeId) throws Exception {
        Store store = storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            Gson gson = new Gson();
            return gson.toJson(store);
        }else {
            throw new Exception("can not show store information");
        }
    }

    public String getStoreDescription(int storeId) throws Exception{
        Store store = storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            return store.getStoreDescription();
        }
        else {
            throw new Exception("can not show store information");
        }
    }

    public void addQuestion(Message m, int storeId) throws Exception {
        storectrl.addQuestion(m, storeId);
    }
    public void setStoreDescription(int storeId,String des) throws Exception{
        Store store = storectrl.getStore(storeId);
        if (store != null )
        {
             store.setStoreDescription(des);
        }
        else {
            throw new Exception("store does not exist");
        }
    }

    public void setStorePurchasePolicy(int storeId,String policy) throws Exception{
        Store store = storectrl.getStore(storeId);
        if (store != null )
        {
            store.setStorePolicy(policy);
        }
        else {
            throw new Exception("store does not exist");
        }
    }




    public int calculatePrice(HashMap<Integer, HashMap<Integer, Integer>> shoppingCart) {
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
        else {
            throw new Exception("store doesn't exist");
        }
    }

    public void setStoreDiscountPolicy(int storeId, String policy) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("miki implement please");
    }

    public void addPurchaseConstraint(int storeId, String constraint) throws Exception {
        Store s;
        if((s= storectrl.getStore(storeId) )!= null){
            s.addPurchaseConstraint(constraint);
        }
        else{
            throw new Exception("store doesn't exist, sorry bruh :(");
        }
    }

    public String getQuestions(int storeId) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(storectrl.getQuestions(storeId));

    }

    public void answerQuestion(int storeId, int questionId, String answer) throws Exception{
        storectrl.answerQuestion(storeId, questionId, answer);
    }

    public String getStoreOrderHistory(int storeId) throws Exception
    {
        Gson gson = new Gson();
        return gson.toJson(storectrl.getStoreOrderHistory(storeId));
    }

    public String getAppointments(int storeId) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(storectrl.getAppointments(storeId));
    }

    public String getStoresInformation() {
        return storectrl.getStoresInformation();
    }

    public Set<Integer> closeStorePermanently(int storeId) throws Exception {
        return storectrl.closeStorePermanently(storeId);
    }

    public void deleteProduct(int storeId, int productId) {
        storectrl.removeProduct(storeId,productId);
    }
}
