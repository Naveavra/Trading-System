package domain.user;


import com.google.gson.Gson;
import org.json.JSONObject;
import utils.infoRelated.Info;
import utils.infoRelated.PrivateInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//TODO: need to decide if to keep security questions in history
public class UserHistory {

    public PurchaseHistory purchaseHistory;
    private List<String> names;
    private transient List<String> passwords;
    private List<String> emails;
    private transient HashMap<String, String> securityQuestions;

    public UserHistory(){
        purchaseHistory = new PurchaseHistory();
        names = new LinkedList<>();
        passwords = new LinkedList<>();
        emails = new LinkedList<>();
        securityQuestions = new HashMap<>();
    }

    public void addPurchaseMade(int orderId, double totalPrice, ShoppingCart purchase){
        purchaseHistory.addPurchaseMade(orderId, totalPrice, purchase);
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
        return purchaseHistory.checkOrderOccurred(orderId);
    }

    public boolean checkOrderContainsStore(int orderId, int storeId){
        return purchaseHistory.checkOrderContainsStore(orderId, storeId);
    }

    public boolean checkOrderContainsProduct(int orderId, int storeId, int productId){
        return purchaseHistory.checkOrderContainsProduct(orderId, storeId, productId);
    }

    public PurchaseHistory getUserPurchaseHistory() {
        return purchaseHistory;
    }

    public PrivateInfo getInformation(Info info) {
        PrivateInfo privateInfo = new PrivateInfo(info, names, emails, passwords, securityQuestions);
        return privateInfo;
    }

    public void changeAnswerForQuestion(String question, String newAnswer) {
        if(securityQuestions.containsKey(question))
            securityQuestions.put(question, newAnswer);
    }

    public void removeSecurityQuestion(String question) {
        securityQuestions.remove(question);
    }

    //for tests
    public  HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>>  getUserPurchaseHistoryHash() {
        return purchaseHistory.getHash();
    }

}
