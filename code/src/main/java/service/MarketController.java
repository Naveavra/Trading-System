package service;

import domain.store.order.OrderController;
import domain.store.product.ProductController;
import domain.store.storeManagement.Store;
import domain.store.storeManagement.StoreController;
import domain.user.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MarketController {

    StoreController storectrl;
    //ProductController productctrl;
    OrderController orderctrl;

    public MarketController()
    {
        storectrl = new StoreController();
        orderctrl = new OrderController();
    }

    public void PurchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingcart, Member user)
    {
        int totalPrice = storectrl.checkPurchaseProducts(shoppingcart);
    }

    public String getProductName(int storeId ,int  productId) throws Exception {
        return storectrl.getProductName(storeId , productId);
    }

    public ArrayList<String> checkMessages(int storeID) throws Exception {

        return storectrl.checkMessages(storeID);
    }
    public void givFeedback(int storeID, int messageID, String feedback ) throws Exception
    {
        storectrl.giveFeedback(storeID, messageID, feedback);
    }




}
