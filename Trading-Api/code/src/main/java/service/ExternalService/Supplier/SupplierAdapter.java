package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;

public interface SupplierAdapter {
    boolean isAvailable();
    void setAvailable(boolean available);
    void orderSupplies(int storeId, int productId, int quantity)  throws Exception;
    int orderSupplies(JSONObject supplyContent, ShoppingCart cart)  throws Exception;

    void checkSupply(ShoppingCart cart) throws Exception;
    void cancelSupply(String transactionId) throws Exception;
}
