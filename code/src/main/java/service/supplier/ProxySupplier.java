package service.supplier;

import service.security.SecurityAdapter;

public class ProxySupplier implements SupplierAdapter {
    private SupplierAdapter real = null;

    //TODO: need to add a list of reals and check when to change to each one
    public void setRealSupplier(SupplierAdapter supplierAdapter){
        if(real == null)
            real = supplierAdapter;
    }
    public void orderSupplies(){}

    @Override
    public void orderSupplies() {

    }
}
