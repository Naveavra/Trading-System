package service.ExternalService.Supplier;

import domain.user.ShoppingCart;
import jdk.jshell.spi.ExecutionControl;
import org.json.JSONObject;
import server.Config.ESConfig;
import service.ExternalService.CancelRequest;
import service.ExternalService.ExternalService;
import service.ExternalService.Request;
import utils.Exceptions.ExternalServiceException;

import java.io.IOException;

/**
 * The ESSupply class represents an external supplier service for ordering supplies.
 */
public class ESSupply extends ExternalService implements SupplierAdapter {
    private final int FAILED = -1;
    private final int MIN_TRANSACTION_ID = 10000;
    private final int MAX_TRANSACTION_ID = 100000;

    /**
     * Constructs an ESSupply object with the specified name and URL.
     *
     * @param name The name of the supplier service.
     * @param url  The URL of the supplier service.
     * @throws IOException              If an I/O error occurs while connecting to the service.
     * @throws ExternalServiceException If the supplier service returns an unexpected response.
     */
    public ESSupply(String name, String url) throws IOException, ExternalServiceException {
        super(name, url);
        handshake();
    }

    /**
     * Constructs an ESSupply object with the specified name, URL, and timeout.
     *
     * @param name           The name of the supplier service.
     * @param url            The URL of the supplier service.
     * @param timeoutSeconds The connection timeout in seconds.
     * @throws IOException              If an I/O error occurs while connecting to the service.
     * @throws ExternalServiceException If the supplier service returns an unexpected response.
     */
    public ESSupply(String name, String url, int timeoutSeconds) throws IOException, ExternalServiceException {
        super(name, url, timeoutSeconds);
        handshake();
    }

    /**
     * Constructs an ESSupply object using the provided ESConfig object.
     *
     * @param payment The ESConfig object containing supplier service configuration.
     * @throws ExternalServiceException If the supplier service returns an unexpected response.
     * @throws IOException              If an I/O error occurs while connecting to the service.
     */
    public ESSupply(ESConfig payment) throws ExternalServiceException, IOException {
        super(payment.getName(), payment.getURL(), payment.getResponseTime());
        handshake();
    }

    /**
     * Orders supplies from the supplier service.
     *
     * @param supplyContent The JSON object containing the supply details.
     * @param cart          The shopping cart containing the items to order.
     * @return The transaction ID associated with the supply order.
     * @throws Exception If an error occurs while ordering supplies.
     */
    @Override
    public int orderSupplies(JSONObject supplyContent, ShoppingCart cart) throws Exception {
        int result;
        String name = supplyContent.getString("name");
        String address = supplyContent.getString("address");
        String city = supplyContent.getString("city");
        String country = supplyContent.getString("country");
        String zip = supplyContent.getString("zip");
        Request supplyRequest = new SupplyRequest(name, address, city, country, zip);
        try {
            result = sendRequest(supplyRequest);
        } catch (Exception e) {
            throw new Exception("Failed to order supplies: " + e.getMessage());
        }
        if (!(MIN_TRANSACTION_ID <= result && result <= MAX_TRANSACTION_ID)) {
            throw new Exception("The supply transaction failed!");
        }
        return result;
    }

    @Override
    public int orderSupplies(JSONObject supplyContent, int storeId, int prodId, int quantity) throws Exception {
        int result;
        String name = supplyContent.getString("name");
        String address = supplyContent.getString("address");
        String city = supplyContent.getString("city");
        String country = supplyContent.getString("country");
        String zip = supplyContent.getString("zip");
        Request supplyRequest = new SupplyRequest(name, address, city, country, zip);
        try {
            result = sendRequest(supplyRequest);
        } catch (Exception e) {
            throw new Exception("Failed to order supplies: " + e.getMessage());
        }
        if (!(MIN_TRANSACTION_ID <= result && result <= MAX_TRANSACTION_ID)) {
            throw new Exception("The supply transaction failed!");
        }
        return result;
    }

    /**
     * Checks the status of a supply order.
     *
     * @param transactionId The transaction ID of the supply order.
     * @throws ExecutionControl.NotImplementedException If the method is not implemented.
     */
    @Override
    public void checkSupply(String transactionId) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Check supply");
    }

    /**
     * Cancels a supply order.
     *
     * @param transactionId The transaction ID of the supply order to cancel.
     * @throws Exception If an error occurs while canceling the supply order.
     */
    @Override
    public void cancelSupply(String transactionId) throws Exception {
        int result = 0;
        Request cancelSupRequest = new CancelRequest("cancel_supply", transactionId);
        try {
            result = sendRequest(cancelSupRequest);
        } catch (Exception e) {
            throw new Exception("Failed to cancel the supply transaction: " + e.getMessage());
        }
        if (result == FAILED) {
            throw new Exception("Failed to cancel the supply transaction!");
        }
    }
}
