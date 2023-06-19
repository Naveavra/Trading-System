package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;

public class MockSupplyService implements SupplierAdapter{
    private boolean failMock;
    private boolean available;

    public MockSupplyService() {
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
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int orderSupplies(JSONObject supplyContent, ShoppingCart cart) throws Exception {
        if(failMock){
            throw new Exception("Error");
        }
        return 12345;
    }

    @Override
    public int orderSupplies(JSONObject supplierDetails, int storeId, int prodId, int quantity) throws Exception {
        if(failMock){
            throw new Exception("Error");
        }
        return 12345;
    }

    @Override
    public void checkSupply(String transactionId) throws Exception {
        if(failMock){
            throw new Exception("Error");
        }
    }

    @Override
    public void cancelSupply(String transactionId) throws Exception {
        if(failMock){
            throw new Exception("Error");
        }
    }
}
