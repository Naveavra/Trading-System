package service;

import utils.ProductInfo;
import utils.StoreInfo;
import utils.orderRelated.Order;
import domain.store.order.OrderController;
import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import jdk.jshell.spi.ExecutionControl;
import utils.messageRelated.Message;
import utils.userInfoRelated.Receipt;

public class MarketController {

    StoreController storectrl;
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

    public int addReviewToStore(Message m) throws Exception {
        return storectrl.writeReviewForStore(m);
    }

    public int writeReviewForProduct(Message m) throws Exception{
        return storectrl.writeReviewForProduct(m);

    }

    public int addProduct(int storeId, String name, String description, int price, int quantity, List<String> categories) throws Exception{
        int id = storectrl.addProduct(storeId,name,description,price,quantity);
        if(id == -1){
            throw new Exception("Something went wrong in adding product");
        }
        storectrl.addToCategory(storeId,id,categories);
        return id;
    }
    public String getProductInformation(int storeId, int productId) throws Exception {
        Store store = storectrl.getStore(storeId);
        if (store != null && store.isActive())
        {
            return store.getProductInformation(productId);
        }
        else {
            throw new Exception("cant get product information");
        }
    }

    public StoreInfo getStoreInformation(int storeId) throws Exception {
        return storectrl.getStoreInformation(storeId);
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

    public int addQuestion(Message m) throws Exception {
        return storectrl.addQuestion(m);
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

    public List<ProductInfo> getStoreProducts(int storeId) throws Exception {
        return storectrl.getProducts(storeId);
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

    public HashMap<Integer, Message> getQuestions(int storeId) throws Exception {
        Gson gson = new Gson();
        return storectrl.getQuestions(storeId);

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

    public void deleteProduct(int storeId, int productId) throws Exception {
        storectrl.removeProduct(storeId,productId);
    }

    public void updateProduct(int storeId, int productId, List<String> categories, String name, String description, int price, int quantity) throws Exception {
        storectrl.updateProduct(storeId,productId,categories,name,description,price,quantity);
    }

    public HashMap<Integer, Message> viewReviews(int storeId) throws Exception {
        return storectrl.viewReviews(storeId);
    }

    public ArrayList<String> showFilterOptions() {
       return storectrl.showFilterOptions();
    }

    /**
     * returns the filtered items by filterOptions.
     * @param filterOptions
     * @return json string representation of the selected products.
     */
    public String filterBy(HashMap<String,String> filterOptions) {
        return storectrl.filterBy(filterOptions);
    }
}
