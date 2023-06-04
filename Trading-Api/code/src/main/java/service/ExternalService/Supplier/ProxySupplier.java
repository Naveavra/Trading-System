package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;
import service.ExternalService.Payment.PaymentAdapter;
import service.ExternalService.Supplier.SupplierAdapter;
import service.ExternalService.WSEPService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProxySupplier implements SupplierAdapter {

    private SupplierAdapter real = null;
    private HashMap<String, SupplierAdapter> supplierServices;

    private List<String> availableSupplierServices;
    //TODO: check by the requirement private HashMap<Integer, String> availablePaymentServices;

    public ProxySupplier() throws Exception {
        this.supplierServices = new HashMap<>();
        this.availableSupplierServices = new ArrayList<>();
        supplierServices.put("WSEP", new WSEPService());
        this.availableSupplierServices.add("WSEP");
        this.real = supplierServices.get("WSEP");
    }

    public List<String> getSupplierServicesAvailableOptions()
    {
        return availableSupplierServices;
    }

    public List<String> getSupplierServicesPossibleOptions()
    {
        return supplierServices.keySet().stream().toList();
    }

    public void addSupplierService(String supplier, SupplierAdapter supplierAdapter) throws Exception {
        if (availableSupplierServices.contains(supplier))
        {
            throw new Exception("This payment service:" + supplier + "already exists!!!");

        }
        else if(!supplierServices.containsKey(supplier))
        {
            throw new Exception("This payment service:" + supplier + "doesn't exists in the possible payment services!!!");
        }
        else{
            supplierServices.put(supplier, supplierAdapter);
            availableSupplierServices.add(supplier);
        }
    }

    public void addSupplierService(String supplierAdapter) throws Exception
    {
        if (availableSupplierServices.contains(supplierAdapter))
        {
            throw new Exception("This supplier service:" + supplierAdapter + "already exists!!!");

        }
        else if(!supplierServices.containsKey(supplierAdapter))
        {
            throw new Exception("This supplier service:" + supplierAdapter + "doesn't exists in the possible supplier services!!!");
        }
        else {
            availableSupplierServices.add(supplierAdapter);
        }
    }

    public void removeSupplierService(String supplierAdapter) throws Exception
    {
        if (availableSupplierServices.size() <= 1)
        {
            throw new IllegalArgumentException("Can't remove supplier service need at least 1 supplier service!");
        }
        else if (!availableSupplierServices.contains(supplierAdapter))
        {
            throw new IllegalArgumentException("This supplier service:" + supplierAdapter + " doesn't exists!!!");
        }
        else
        {
            availableSupplierServices.remove(supplierAdapter);
        }
    }


    public void setRealSupplier(String supplierAdapter) throws Exception{
        if(availableSupplierServices.contains(supplierAdapter)) {
            real = supplierServices.get(supplierAdapter);
        }
        else{
            throw new Exception("The " + supplierAdapter + "doesn't exist!");
        }
    }

    @Override
    public void orderSupplies(int storeId, int productId, int quantity) throws Exception {
        if (real != null){
            real.orderSupplies(storeId, productId, quantity);
        }
    }

    @Override
    public int orderSupplies(JSONObject supplyContent) throws Exception {
        if (real != null){
            return real.orderSupplies(supplyContent);
        }
        return -1;
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

    public void orderSupplies(String supplier, int storeId, int productId, int quantity) throws Exception {
        if (availableSupplierServices.contains(supplier)){
            supplierServices.get(supplier).orderSupplies(storeId, productId, quantity);
        }
    }
}
