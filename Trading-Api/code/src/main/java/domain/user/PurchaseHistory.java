package domain.user;

import org.json.JSONObject;
import utils.Pair;
import utils.infoRelated.Information;

import java.util.HashMap;
import java.util.List;

public class PurchaseHistory extends Information {

    private HashMap<Integer, ShoppingCart> purchaseHistory;
    private HashMap <Integer, Double> ordersAndPrices;

    public PurchaseHistory(){
        purchaseHistory = new HashMap<>();
        ordersAndPrices = new HashMap<>();
    }

    public void addPurchaseMade(int orderId, double totalPrice, ShoppingCart purchase){
        ShoppingCart add = new ShoppingCart(purchase);
        purchaseHistory.put(orderId, add);
        ordersAndPrices.put(orderId, totalPrice);
    }

    //TODO: add toJson
    @Override
    public JSONObject toJson() {
        return null;
    }

    public boolean checkOrderOccurred(int orderId) {
        return purchaseHistory.containsKey(orderId);
    }

    public boolean checkOrderContainsStore(int orderId, int storeId) {
        if(purchaseHistory.containsKey(orderId))
            return purchaseHistory.get(orderId).hasStore(storeId);
        return false;
    }

    public boolean checkOrderContainsProduct(int orderId, int storeId, int productId) {
        if(purchaseHistory.containsKey(orderId))
            return purchaseHistory.get(orderId).hasProduct(storeId, productId);
        return false;
    }

    public int getHisSize(){
        return purchaseHistory.size();
    }

    public HashMap<Integer, ShoppingCart> getPurchaseHistory(){
        return purchaseHistory;
    }
}
