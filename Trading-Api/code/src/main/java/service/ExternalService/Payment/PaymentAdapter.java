package service.ExternalService.Payment;

import org.json.JSONObject;

public interface PaymentAdapter {
    boolean isAvailable();
    void setAvailable(boolean available);

    public void makePurchase(String accountNumber , double amount) throws Exception;

    int makePurchase(JSONObject json, double price) throws Exception;

    public void cancelPurchase(String transactionId) throws Exception;
}
