package domain.user;

import org.json.JSONObject;
import utils.Pair;
import utils.infoRelated.Information;

import java.util.HashMap;

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

    public HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> getHash(){
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> ans = new HashMap<>();
        for(int orderId : purchaseHistory.keySet())
            ans.put(orderId, purchaseHistory.get(orderId).getContent());
        return ans;
    }

}
