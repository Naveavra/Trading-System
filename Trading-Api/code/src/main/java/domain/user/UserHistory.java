package domain.user;


import com.google.gson.Gson;
import utils.Pair;
import utils.userInfoRelated.PrivateInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//TODO: need to decide if to keep security questions in history
public class UserHistory {

    //the hashmap shows from orderId to the shopping cart content
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> purchaseHistory;
    private HashMap <Integer, Double> ordersAndPrices;
    private List<String> names;
    private transient List<String> passwords;
    private List<String> emails;
    private transient HashMap<String, String> securityQuestions;

    public UserHistory(){
        purchaseHistory = new HashMap<>();
        ordersAndPrices = new HashMap<>();
        names = new LinkedList<>();
        passwords = new LinkedList<>();
        emails = new LinkedList<>();
        securityQuestions = new HashMap<>();
    }

    public void addPurchaseMade(int orderId, double totalPrice, HashMap<Integer, HashMap<Integer, Integer>> purchase){
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

    public void addQuestion(String question, String answer){
        securityQuestions.put(question, answer);
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

    public  HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>getUserPurchaseHistory() { //NAVE
        Gson gson = new Gson();
        return purchaseHistory;
    }

    public void getInformation(PrivateInfo info) {
        info.addOldNames(names);
        info.addOldEmails(emails);
        info.addOldPasswords(passwords);
        info.addSecurityQuestion(securityQuestions);
    }

    public void changeAnswerForQuestion(String question, String newAnswer) {
        if(securityQuestions.containsKey(question))
            securityQuestions.put(question, newAnswer);
    }

    public void removeSecurityQuestion(String question) {
        securityQuestions.remove(question);
    }

}
