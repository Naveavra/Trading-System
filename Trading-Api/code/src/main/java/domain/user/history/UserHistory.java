package domain.user.history;


import domain.user.ShoppingCart;

//TODO: need to decide if to keep security questions in history
public class UserHistory {

    private int userId;
    private PurchaseHistory purchaseHistory;

    public UserHistory(int userId, String email, String pass){
        this.userId = userId;
        purchaseHistory = new PurchaseHistory(userId);
    }

    public void addPurchaseMade(int orderId, double totalPrice, ShoppingCart purchase){
        purchaseHistory.addPurchaseMade(userId, orderId, totalPrice, purchase);
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

}
