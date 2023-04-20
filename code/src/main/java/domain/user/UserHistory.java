package domain.user;

import java.util.HashMap;

public class UserHistory {

    public HashMap<Integer, String> purchaseHistory; //may change to Json and not string

    public void addPurchaseMade(int orderId, String purchase){
        purchaseHistory.put(orderId, purchase);
    }

    public boolean checkOrderOccurred(int orderId){
        return purchaseHistory.containsKey(orderId);
    }

}
