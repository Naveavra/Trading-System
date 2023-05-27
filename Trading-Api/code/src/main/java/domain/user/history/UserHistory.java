package domain.user.history;


import domain.user.ShoppingCart;
import domain.user.history.PurchaseHistory;
import utils.infoRelated.Info;
import utils.infoRelated.PrivateInfo;

import java.util.LinkedList;
import java.util.List;

//TODO: need to decide if to keep security questions in history
public class UserHistory {

    private int userId;
    private PurchaseHistory purchaseHistory;
    private List<String> names;
    private transient List<String> passwords;
    private List<String> emails;

    public UserHistory(int userId, String email, String name, String pass){
        this.userId = userId;
        purchaseHistory = new PurchaseHistory(userId);
        names = new LinkedList<>();
        passwords = new LinkedList<>();
        emails = new LinkedList<>();
        addEmail(email);
        addName(name);
        addPassword(pass);
    }

    public void addPurchaseMade(int orderId, double totalPrice, ShoppingCart purchase){
        purchaseHistory.addPurchaseMade(userId, orderId, totalPrice, purchase);
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
        PrivateInfo privateInfo = new PrivateInfo(info, names, emails, passwords);
        return privateInfo;
    }
}
