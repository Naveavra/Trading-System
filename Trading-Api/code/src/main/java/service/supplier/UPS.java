package service.supplier;

public class UPS implements SupplierAdapter {
    @Override
    public void orderSupplies(int storeId, int productId, int quantity) throws Exception {
        throw new IllegalArgumentException("Loss Connection");
    }

}
