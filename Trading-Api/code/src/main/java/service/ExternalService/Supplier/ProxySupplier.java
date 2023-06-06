package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;
import service.ExternalService.Payment.PaymentAdapter;
import service.ExternalService.Supplier.SupplierAdapter;
import service.ExternalService.WSEPService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ProxySupplier implements SupplierAdapter {

    private SupplierAdapter real = null;
    private HashMap<String, SupplierAdapter> supplierServices;



    public ProxySupplier() throws Exception {
        this.supplierServices = new HashMap<>();
        supplierServices.put("WSEP", new WSEPService());
        supplierServices.put("Mock", new SupplierAdapterMock());
        this.real = supplierServices.get("WSEP");
        this.real.setAvailable(true);
    }

    public List<String> getSupplierServicesAvailableOptions()
    {
        return supplierServices.entrySet()
                .stream()
                .filter(sup -> sup.getValue().isAvailable())
                .map(sup -> sup.getKey())
                .collect(Collectors.toList());
    }

    public List<String> getSupplierServicesPossibleOptions()
    {
        return supplierServices.keySet().stream().toList();
    }

    public void addSupplierService(String supplier, SupplierAdapter supplierAdapter) throws Exception {
        if (supplierServices.containsKey(supplier))
        {
            throw new Exception("This supplier service:" + supplier + "doesn't exists in the possible payment services!!!");
        }
        else{
            supplierServices.put(supplier, supplierAdapter);
            supplierAdapter.setAvailable(true);
        }
    }

    public void addSupplierService(String supplier) throws Exception
    {
        if (!supplierServices.containsKey(supplier))
        {
            throw new Exception("This supplier service:" + supplier + "doesn't exists in the possible payment services!!!");
        }
        else if(supplierServices.get(supplier).isAvailable())
        {
            throw new Exception("This supplier service:" + supplier + "already available exists!!!");
        }
        else{
            supplierServices.get(supplier).setAvailable(true);
        }
    }

    public void removeSupplierService(String supplierAdapter) throws Exception
    {
        if (getSupplierServicesAvailableOptions().size() <= 1)
        {
            throw new IllegalArgumentException("Can't remove supplier service need at least 1 supplier service!");
        }
        else if (!supplierServices.containsKey(supplierAdapter) || !supplierServices.get(supplierAdapter).isAvailable())
        {
            throw new IllegalArgumentException("This supplier service:" + supplierAdapter + " doesn't available or exists!!!");
        }
        else
        {
            supplierServices.get(supplierAdapter).setAvailable(false);
        }
    }


    public void setRealSupplier(String supplierAdapter) throws Exception{
        if(supplierServices.containsKey(supplierAdapter) &&
                supplierServices.get(supplierAdapter).isAvailable()) {
            real = supplierServices.get(supplierAdapter);
        }
        else{
            throw new Exception("The " + supplierAdapter + "doesn't available or exist!");
        }
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void setAvailable(boolean available) {

    }

    @Override
    public void orderSupplies(int storeId, int productId, int quantity) throws Exception {
        if (real != null){
            real.orderSupplies(storeId, productId, quantity);
        }
    }

    @Override
    public int orderSupplies(JSONObject supplyContent, ShoppingCart cart) throws Exception {
        String supplier = supplyContent.getString("supply_service");
        if(supplierServices.containsKey(supplier) &&
                supplierServices.get(supplier).isAvailable()) {
            return supplierServices.get(supplier).orderSupplies(supplyContent, cart);
        }
        throw new Exception("The " + supplier + "doesn't available or exist!");
    }

    @Override
    public void checkSupply(ShoppingCart cart) throws Exception{
        if (real != null){
            real.checkSupply(cart);
        }
    }

    @Override
    public void cancelSupply(String transactionId) throws Exception {
        if (real != null){
            real.cancelSupply(transactionId);
        }
    }

}
