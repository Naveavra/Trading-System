package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;
import server.Config.ESConfig;
import service.ExternalService.Payment.ESPayment;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ProxySupplier class acts as a proxy for multiple supplier services.
 */
public class ProxySupplier implements SupplierAdapter {
    private SupplierAdapter real = null;
    private HashMap<String, SupplierAdapter> supplierServices;

    /**
     * Constructs a ProxySupplier object using the provided ESConfig object.
     *
     * @param supply The ESConfig object containing supplier service configuration.
     * @throws Exception If an error occurs while initializing the proxy supplier.
     */
    public ProxySupplier(ESConfig supply) throws Exception {
        this.supplierServices = new HashMap<>();
        ESSupply es = new ESSupply(supply);
        this.supplierServices.put(es.getName(), es);
        this.real = es;
        es.setAvailable(true);
    }

    /**
     * Retrieves the available options for supplier services.
     *
     * @return A list of supplier service options.
     */
    public List<String> getSupplierServicesAvailableOptions() {
        return supplierServices.entrySet()
                .stream()
                .filter(sup -> sup.getValue().isAvailable())
                .map(sup -> sup.getKey())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all possible supplier service options.
     *
     * @return A list of all supplier service options.
     */
    public List<String> getSupplierServicesPossibleOptions() {
        return supplierServices.keySet().stream().toList();
    }

    /**
     * Adds a supplier service to the proxy supplier.
     *
     * @param supplier        The name of the supplier service.
     * @param supplierAdapter The supplier adapter implementation.
     * @throws Exception If the supplier service doesn't exist or is already available.
     */
    public void addSupplierService(String supplier, SupplierAdapter supplierAdapter) throws Exception {
        if (supplierServices.containsKey(supplier)) {
            throw new Exception("This supplier service: " + supplier + " doesn't exist in the possible supplier services!");
        } else {
            supplierServices.put(supplier, supplierAdapter);
            supplierAdapter.setAvailable(true);
        }
    }

    /**
     * Adds a supplier service to the proxy supplier.
     *
     * @param supplier The name of the supplier service.
     * @throws Exception If the supplier service doesn't exist or is already available.
     */
    public void addSupplierService(String supplier) throws Exception {
        if (!supplierServices.containsKey(supplier)) {
            throw new Exception("This supplier service: " + supplier + " doesn't exist in the possible supplier services!");
        } else if (supplierServices.get(supplier).isAvailable()) {
            throw new Exception("This supplier service: " + supplier + " is already available!");
        } else {
            supplierServices.get(supplier).setAvailable(true);
        }
    }

    /**
     * Removes a supplier service from the proxy supplier.
     *
     * @param supplierAdapter The name of the supplier service to remove.
     * @throws Exception If there is only one available supplier service or the supplier service doesn't exist or is not available.
     */
    public void removeSupplierService(String supplierAdapter) throws Exception {
        if (getSupplierServicesAvailableOptions().size() <= 1) {
            throw new IllegalArgumentException("Can't remove supplier service. At least 1 supplier service is required!");
        } else if (!supplierServices.containsKey(supplierAdapter) || !supplierServices.get(supplierAdapter).isAvailable()) {
            throw new IllegalArgumentException("This supplier service: " + supplierAdapter + " doesn't exist or is not available!");
        } else {
            supplierServices.get(supplierAdapter).setAvailable(false);
        }
    }

    /**
     * Sets the real supplier to use for order supplies.
     *
     * @param supplierAdapter The name of the supplier service to set as the real supplier.
     * @throws Exception If the supplier service is not available or doesn't exist.
     */
    public void setRealSupplier(String supplierAdapter) throws Exception {
        if (supplierServices.containsKey(supplierAdapter) &&
                supplierServices.get(supplierAdapter).isAvailable()) {
            real = supplierServices.get(supplierAdapter);
        } else {
            throw new Exception("The supplier service " + supplierAdapter + " is not available or does not exist!");
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
    public int orderSupplies(JSONObject supplyContent, ShoppingCart cart) throws Exception {
        String supplier = supplyContent.getString("supply_service");
        if (supplierServices.containsKey(supplier) &&
                supplierServices.get(supplier).isAvailable()) {
            return supplierServices.get(supplier).orderSupplies(supplyContent, cart);
        }
        throw new Exception("The supplier service " + supplier + " is not available or does not exist!");
    }

    @Override
    public void checkSupply(String transactionId) throws Exception {

    }

    @Override
    public void cancelSupply(String transactionId) throws Exception {
        if (real != null) {
            real.cancelSupply(transactionId);
        }
    }
}
