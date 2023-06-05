package service.ExternalService;

import domain.user.ShoppingCart;
import org.json.JSONObject;
import service.ExternalService.Payment.PaymentAdapter;
import service.ExternalService.Payment.PaymentRequest;
import service.ExternalService.Supplier.SupplierAdapter;
import service.ExternalService.Supplier.SupplyRequest;

public class WSEPService extends ExternalService implements PaymentAdapter, SupplierAdapter {

    private final int MAX_TRANSACTION_ID =  100000;
    private final int MIN_TRANSACTION_ID =  10000;
    private final int FAILED =  -1;
    private boolean available;

    public WSEPService() throws Exception {
        super("https://php-server-try.000webhostapp.com/");
        available = false;
    }

    @Override
    public void makePurchase(String accountNumber, double amount) throws Exception {

    }

    @Override
    public int makePurchase(JSONObject userDetails) throws Exception {
        int result;
        String cardNumber = userDetails.get("cardNumber").toString();
        String month = userDetails.get("month").toString();
        String year = userDetails.get("year").toString();
        String holder = userDetails.get("holder").toString();
        String ccv = userDetails.get("ccv").toString();
        String id = userDetails.get("id").toString();
        Request paymentRequest = new PaymentRequest(cardNumber, month, year, holder, ccv, id);
        handshake();
        result = sendRequest(paymentRequest);
        if (!(MIN_TRANSACTION_ID <= result && result <= MAX_TRANSACTION_ID))
        {
            throw new Exception("The payment transaction failed!");
        }
        return result;
    }

    @Override
    public void cancelPurchase(String transactionId) throws Exception {
        int result = 0;
        Request cancelPayRequest = new CancelRequest("cancel_pay", transactionId);
        result = sendRequest(cancelPayRequest);
        if (result == FAILED)
        {
            throw new Exception("Failed to cancel the payment transaction!");
        }
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
    public void orderSupplies(int storeId, int productId, int quantity) throws Exception {

    }

    @Override
    public int orderSupplies(JSONObject supplyContent) throws Exception {
        int result;
        String name = supplyContent.get("name").toString();
        String address = supplyContent.get("address").toString();
        String city = supplyContent.get("city").toString();
        String country = supplyContent.get("country").toString();
        String zip = supplyContent.get("zip").toString();
        Request supplyRequest = new SupplyRequest(name, address, city, country, zip);
        result = sendRequest(supplyRequest);
        if (!(MIN_TRANSACTION_ID <= result && result <= MAX_TRANSACTION_ID))
        {
            throw new Exception("The payment transaction failed!");
        }
        return result;
    }

    @Override
    public void checkSupply(ShoppingCart cart) throws Exception {
        throw new Exception("This feature is not supported by this service.");
    }

    @Override
    public void cancelSupply(String transactionId) throws Exception {
        int result = 0;
        Request cancelSupRequest = new CancelRequest("cancel_supply", transactionId);
        result = sendRequest(cancelSupRequest);
        if (result == FAILED)
        {
            throw new Exception("Failed to cancel the payment transaction!");
        }
    }
}
