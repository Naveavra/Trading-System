package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;

public class SupplierAdapterMock implements SupplierAdapter{
    private final int TRANSACTION_NUMBER = 55555;
    private boolean work;
    private boolean available;

    public SupplierAdapterMock() {
        available = false;
        work = false;
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
        if (!work){
            throw new Exception("Can't do order supplies this service doesn't work!");
        }
    }

    @Override
    public int orderSupplies(JSONObject supplyContent, ShoppingCart cart) throws Exception {
        work = supplyContent.getString("Mock").equals("on");
        if (!work){
            throw new Exception("Can't do order supplies this service doesn't work!");
        }
        return TRANSACTION_NUMBER;
    }

    @Override
    public void checkSupply(ShoppingCart cart) throws Exception {
        if (!work){
            throw new Exception("Can't do check supply this service doesn't work!");
        }
    }

    @Override
    public void cancelSupply(String transactionId) throws Exception {
        if (!work){
            throw new Exception("Can't do cancel supply this service doesn't work!");
        }
    }
}
