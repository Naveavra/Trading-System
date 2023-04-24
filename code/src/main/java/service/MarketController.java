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




}
