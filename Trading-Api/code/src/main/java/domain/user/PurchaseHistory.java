package domain.user;

import domain.user.ShoppingCart;
import org.json.JSONObject;
import org.json.JSONPointer;
import utils.Pair;
import utils.Response;
import utils.infoRelated.Information;
import utils.infoRelated.Receipt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseHistory{

    private int userId;
    private HashMap<Integer, Receipt> purchaseHistory;

    public PurchaseHistory(int userId){
        this.userId = userId;
        purchaseHistory = new HashMap<>();
    }

    public void addPurchaseMade(int orderId, double totalPrice, ShoppingCart purchase){
        ShoppingCart add = new ShoppingCart(purchase);
        Receipt receipt = new Receipt(userId, orderId, add, totalPrice);
        purchaseHistory.put(orderId, receipt);
    }

    public List<Receipt> getReceipts(){
        List<Receipt> receipts = new ArrayList<>(purchaseHistory.values());
        return receipts;
    }

    public boolean checkOrderOccurred(int orderId) {
        return purchaseHistory.containsKey(orderId);
    }

    public boolean checkOrderContainsStore(int orderId, int storeId) {
        if(checkOrderOccurred(orderId))
            return purchaseHistory.get(orderId).getCart().hasStore(storeId);
        return false;
    }

    public boolean checkOrderContainsProduct(int orderId, int storeId, int productId) {
        if(purchaseHistory.containsKey(orderId))
            return purchaseHistory.get(orderId).getCart().hasProduct(storeId, productId);
        return false;
    }

    public int getHisSize(){
        return purchaseHistory.size();
    }

    public HashMap<Integer, Receipt> getPurchaseHistory(){
        return purchaseHistory;
    }
}
