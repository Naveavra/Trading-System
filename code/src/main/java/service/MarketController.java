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
    UserController userCtrl;
    public MarketController()
    {
        storectrl = new StoreController();
        orderctrl = new OrderController();
        userCtrl = new UserController();
    }

    public void PurchaseProducts(HashMap<Integer, HashMap<Integer, Integer>> shoppingcart, Member user)
    {
        int totalPrice = storectrl.checkPurchaseProducts(shoppingcart);
    }

    /**
     * @param userID creator id
     * @param description store description
     */
    public void openStore(int userID, String description) throws Exception
    {
        if (!userCtrl.canOpenStore(userID))
        {
            throw new Exception("user cant open a store");
        }
        Store store = storectrl.openStore(description, userID);
        userCtrl.openStore(userID, store);

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
