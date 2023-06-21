package domain.user;

import database.daos.Dao;
import org.hibernate.Session;
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

    public void addPurchaseMade(Receipt receipt) throws Exception{
        purchaseHistory.put(receipt.getOrderId(), receipt);
    }

    public Receipt getReceipt(int orderId){
        return purchaseHistory.get(orderId);
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
        if(checkOrderOccurred(orderId))
            return purchaseHistory.get(orderId).getCart().hasProduct(storeId, productId);
        return false;
    }

    public int getHisSize(){
        return purchaseHistory.size();
    }

    public HashMap<Integer, Receipt> getPurchaseHistory(){
        return purchaseHistory;
    }

    public void removeReceipt(int orderId) {
        purchaseHistory.remove(orderId);
    }
}
