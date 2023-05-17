package service.supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProxySupplier implements SupplierAdapter {

    private SupplierAdapter real = null;
    private HashMap<String, SupplierAdapter> supplierServices;

    private List<String> availableSupplierServices;
    //TODO: check by the requirement private HashMap<Integer, String> availablePaymentServices;

    public ProxySupplier(String real) {
        this.supplierServices = new HashMap<>();
        this.availableSupplierServices = new ArrayList<>();
        this.supplierServices.put("DHL", new DHL());
        this.supplierServices.put("UPS", new UPS());
        this.availableSupplierServices.add(real);
        this.real = supplierServices.get(real);
    }

    public List<String> getSupplierServicesAvailableOptions()
    {
        return availableSupplierServices;
    }

    public List<String> getSupplierServicesPossibleOptions()
    {
        return supplierServices.keySet().stream().toList();
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
        if (availableSupplierServices.size() > 1)
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

    public void orderSupplies(String supplier, int storeId, int productId, int quantity) throws Exception {
        if (availableSupplierServices.contains(supplier)){
            supplierServices.get(supplier).orderSupplies(storeId, productId, quantity);
        }
    }
}
