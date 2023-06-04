package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;

public interface SupplierAdapter {

    public void orderSupplies(int storeId, int productId, int quantity)  throws Exception;
    public int orderSupplies(JSONObject supplyContent)  throws Exception;

    void checkSupply(ShoppingCart cart) throws Exception;

    public void cancelSupply(String transactionId) throws Exception;
}
