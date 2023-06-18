package service.ExternalService.Payment;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

public class OurPaymentService implements PaymentAdapter{
    private static final int START_TRANSACTION_ID = 1;
    private static AtomicInteger transactionIds;
    private boolean available;
    private String name;


    public OurPaymentService() {
        name = "ELI - Payment Service";
        transactionIds = new AtomicInteger(START_TRANSACTION_ID);
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public int makePurchase(JSONObject userDetails, JSONObject storeDetails, double price) throws Exception {
        int transactionId = transactionIds.getAndIncrement();
        return transactionId;
    }

    @Override
    public void cancelPurchase(String transactionId) throws Exception {

    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }
}
