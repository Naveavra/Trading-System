package service.ExternalService.Payment;

import org.json.JSONObject;

public interface PaymentAdapter {

    public void makePurchase(String accountNumber , double amount) throws Exception;
    public int makePurchase(JSONObject json) throws Exception;
    public void cancelPurchase(String transactionId) throws Exception;
}
