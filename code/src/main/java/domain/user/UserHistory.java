package domain.user;


import java.util.HashMap;

public class UserHistory {

    //the hashmap shows from orderId to the shopping cart content
    public HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> purchaseHistory;

    public UserHistory(){
        purchaseHistory = new HashMap<>();
    }

    public void addPurchaseMade(int orderId, HashMap<Integer, HashMap<Integer, Integer>> purchase){
        purchaseHistory.put(orderId, purchase);
    }

    public boolean checkOrderOccurred(int orderId){
        return purchaseHistory.containsKey(orderId);
    }

    public boolean checkOrderContainsStore(int orderId, int storeId){
        if(purchaseHistory.containsKey(orderId))
            if(purchaseHistory.get(orderId).containsKey(storeId))
                return true;
        return false;
    }

    public boolean checkOrderContainsProduct(int orderId, int storeId, int productId){
        if(purchaseHistory.containsKey(orderId))
            if(purchaseHistory.get(orderId).containsKey(storeId))
                if(purchaseHistory.get(orderId).get(storeId).containsKey(productId))
                    return true;
        return false;
    }

}
