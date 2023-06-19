package service.ExternalService.Payment;

import org.json.JSONObject;

public class MockPaymentService implements PaymentAdapter{
    private boolean failMock;
    private boolean available;

    public MockPaymentService() {
        failMock = false;
    }

    public void setFailMock(boolean failMock) {
        this.failMock = failMock;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public int makePurchase(JSONObject userDetails, JSONObject storeDetails, double price) throws Exception {
        if(failMock){
            throw new Exception("Error");
        }
        return 12345;
    }

    @Override
    public void cancelPurchase(String transactionId) throws Exception {
        if(failMock){
            throw new Exception("Error");
        }
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
