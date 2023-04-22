package domain.user;


import utils.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserHistory {

    //the hashmap shows from orderId to the shopping cart content
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> purchaseHistory;
    private HashMap <Integer, Integer> ordersAndPrices;
    private List<String> names;
    private List<String> passwords;
    private List<String> emails;
    private List<Pair<String, String>> securityQuestions;

    public UserHistory(){
        purchaseHistory = new HashMap<>();
        ordersAndPrices = new HashMap<>();
        names = new LinkedList<>();
        passwords = new LinkedList<>();
        emails = new LinkedList<>();
        securityQuestions = new LinkedList<>();
    }

    public void addPurchaseMade(int orderId, int totalPrice, HashMap<Integer, HashMap<Integer, Integer>> purchase){
        purchaseHistory.put(orderId, purchase);
        ordersAndPrices.put(orderId, totalPrice);
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

    public String getUserPurchaseHistory(String name) {
        String purchases = "purchase history for user " + name + ":\n";
        for(int orderId : purchaseHistory.keySet()){
            purchases = purchases+"  orderId: " + orderId +"\n";
            for(int storeId : purchaseHistory.get(orderId).keySet()){
                purchases = purchases + "    storeId: " + storeId + "\n";
                for(int productId : purchaseHistory.get(orderId).get(storeId).keySet()) {
                    purchases = purchases + "      proudctId: " + productId + ", quantity: " + purchaseHistory.get(orderId).get(storeId).get(productId) + "\n";
                }
            }
            purchases = purchases + "  the total price was: " + ordersAndPrices.get(orderId) + "\n";
        }
       return purchases;
    }
}
