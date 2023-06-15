package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import org.json.JSONObject;

/**
 * The SupplierAdapter interface defines the contract for interacting with a supplier service.
 */
public interface SupplierAdapter {
    /**
     * Checks if the supplier service is available.
     *
     * @return `true` if the supplier service is available, `false` otherwise.
     */
    boolean isAvailable();

    /**
     * Sets the availability of the supplier service.
     *
     * @param available `true` to set the supplier service as available, `false` otherwise.
     */
    void setAvailable(boolean available);

    /**
     * Places an order for supplies from the supplier.
     *
     * @param supplyContent The content of the supply order.
     * @param cart          The shopping cart containing the items to be ordered.
     * @return The transaction ID or order ID associated with the supply order.
     * @throws Exception If an error occurs during the order placement.
     */
    int orderSupplies(JSONObject supplyContent, ShoppingCart cart) throws Exception;

    /**
     * Checks the status of a supply transaction with the supplier.
     *
     * @param transactionId The transaction ID or order ID to check.
     * @throws Exception If an error occurs during the status check.
     */
    void checkSupply(String transactionId) throws Exception;

    /**
     * Cancels a supply transaction with the supplier.
     *
     * @param transactionId The transaction ID or order ID to cancel.
     * @throws Exception If an error occurs during the cancellation.
     */
    void cancelSupply(String transactionId) throws Exception;
}
