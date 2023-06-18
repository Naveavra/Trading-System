package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

public class OurSupplyService implements SupplierAdapter{
    private static final int START_TRANSACTION_ID = 1;
    private static AtomicInteger transactionIds;
    private boolean available;
    private String name;

    public OurSupplyService() {
        name = "ZIV - Supply Service";
        transactionIds = new AtomicInteger(START_TRANSACTION_ID);
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
    public int orderSupplies(JSONObject supplyContent, ShoppingCart cart) throws Exception {
        int transactionId = transactionIds.getAndIncrement();
        return transactionId;
    }

    @Override
    public int orderSupplies(JSONObject supplierDetails, int storeId, int prodId, int quantity) throws Exception {
        int transactionId = transactionIds.getAndIncrement();
        return transactionId;
    }

    @Override
    public void checkSupply(String transactionId) throws Exception {

    }

    @Override
    public void cancelSupply(String transactionId) throws Exception {

    }

    public String getName() {
        return name;
    }
}
