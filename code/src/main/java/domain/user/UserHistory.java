package domain.user;


import com.google.gson.Gson;
import utils.Pair;
import utils.PrivateInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserHistory {

    //the hashmap shows from orderId to the shopping cart content
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> purchaseHistory;
    private HashMap <Integer, Integer> ordersAndPrices;
    private List<String> names;
    private transient List<String> passwords;
    private List<String> emails;
    private transient List<Pair<String, String>> securityQuestions;

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

    public void addName(String name){
        names.add(name);
    }

    public void addPassword(String password){
        passwords.add(password);
    }
    public void addEmail(String email){
        emails.add(email);
    }

    public void addQuestion(Pair<String, String> question){
        securityQuestions.add(question);
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

    public String getUserPurchaseHistory(String name) { //NAVE
        Gson gson = new Gson();
        return gson.toJson(purchaseHistory);
//        String purchases = "purchase history for user " + name + ":\n";
//        for(int orderId : purchaseHistory.keySet()){
//            purchases = purchases+"  orderId: " + orderId +"\n";
//            for(int storeId : purchaseHistory.get(orderId).keySet()){
//                purchases = purchases + "    storeId: " + storeId + "\n";
//                for(int productId : purchaseHistory.get(orderId).get(storeId).keySet()) {
//                    purchases = purchases + "      proudctId: " + productId + ", quantity: " + purchaseHistory.get(orderId).get(storeId).get(productId) + "\n";
//                }
//            }
//            purchases = purchases + "  the total price was: " + ordersAndPrices.get(orderId) + "\n";
//        }
//       return purchases;
    }

    public void getInformation(PrivateInfo info) {
        info.addOldNames(names);
        info.addOldEmails(emails);
        info.addOldPasswords(passwords);
    }

    public void changeAnswerForQuestion(String question, String newAnswer) {
        for(Pair<String, String> secQuestion : securityQuestions)
            if(secQuestion.getFirst().equals(question))
                secQuestion.setSecond(newAnswer);
    }
}
