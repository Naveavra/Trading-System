package service;

import domain.store.order.Order;
import domain.store.order.OrderController;
import domain.store.product.ProductController;
import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;
import domain.user.Member;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.Gson;
import utils.Message;

import java.util.concurrent.atomic.AtomicInteger;

public class MarketController {

    StoreController storectrl;
    //ProductController productctrl;
    OrderController orderctrl;
    UserController userCtrl;
    public MarketController()
    {
        storectrl = new StoreController();
        orderctrl = new OrderController();
        userCtrl = new UserController();
    }

    public void PurchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingcart, Member user)
    {
        int totalPrice = storectrl.createOrder(shoppingcart);
        Order order = orderctrl.createNewOrder(user.getId(),shoppingcart);
        order.setTotalPrice(totalPrice);
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

    public String


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

    public String writeReviewForProduct(Message m) throws Exception{
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

    public String addQuestion(Message m, int storeId) throws Exception{
        return storectrl.addQuestion(m, storeId);

    }
}
