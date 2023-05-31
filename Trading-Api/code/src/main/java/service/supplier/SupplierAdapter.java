package service.supplier;

import domain.user.ShoppingCart;

public interface SupplierAdapter {

    public void orderSupplies(int storeId, int productId, int quantity)  throws Exception;

    void checkSupply(ShoppingCart cart) throws Exception;
}
