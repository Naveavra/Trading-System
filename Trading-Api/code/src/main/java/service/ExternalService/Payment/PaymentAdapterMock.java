package service.ExternalService.Payment;

import org.json.JSONObject;
import org.mockito.Mockito;

public class PaymentAdapterMock implements PaymentAdapter{
    private final int TRANSACTION_NUMBER = 55555;
    private boolean work;
    private boolean available;

    public PaymentAdapterMock() {
        available = false;
        work = false;
    }

    public void setMock(boolean work)
    {
        this.work = work;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public void makePurchase(String accountNumber, double amount) throws Exception {
        if (!work){
            throw new Exception("Can't do make purchase this service doesn't work!");
        }
    }

    @Override
    public int makePurchase(JSONObject json, double price) throws Exception {
        work = json.getString("Mock").equals("on");
        if (!work){
            throw new Exception("Can't do make purchase this service doesn't work!");
        }
        return TRANSACTION_NUMBER;
    }

    @Override
    public void cancelPurchase(String transactionId) throws Exception {
        if (!work){
            throw new Exception("Can't do cancel purchase this service doesn't work!");
        }
    }
}
