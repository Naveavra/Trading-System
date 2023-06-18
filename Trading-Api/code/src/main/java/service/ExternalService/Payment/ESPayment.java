package service.ExternalService.Payment;

import org.json.JSONObject;
import server.Config.ESConfig;
import service.ExternalService.CancelRequest;
import service.ExternalService.ExternalService;
import service.ExternalService.Request;
import utils.Exceptions.ExternalServiceException;

import java.io.IOException;

/**
 * The ESPayment class represents an external payment service adapter.
 * It extends the ExternalService class and implements the PaymentAdapter interface.
 */
public class ESPayment extends ExternalService implements PaymentAdapter {
    private final int FAILED = -1;
    private final int MIN_TRANSACTION_ID = 10000;
    private final int MAX_TRANSACTION_ID = 100000;
    private boolean available;

    /**
     * Constructs an ESPayment object with the specified name and URL.
     *
     * @param name The name of the payment service.
     * @param url  The URL of the payment service.
     * @throws IOException              If an I/O error occurs while connecting to the service.
     * @throws ExternalServiceException If the payment service returns an unexpected response.
     */
    public ESPayment(String name, String url) throws IOException, ExternalServiceException {
        super(name, url);
        handshake();
    }

    /**
     * Constructs an ESPayment object with the specified name, URL, and timeout.
     *
     * @param name           The name of the payment service.
     * @param url            The URL of the payment service.
     * @param timeoutSeconds The connection timeout in seconds.
     * @throws IOException              If an I/O error occurs while connecting to the service.
     * @throws ExternalServiceException If the payment service returns an unexpected response.
     */
    public ESPayment(String name, String url, int timeoutSeconds) throws IOException, ExternalServiceException {
        super(name, url, timeoutSeconds);
        handshake();
    }

    /**
     * Constructs an ESPayment object using the provided ESConfig object.
     *
     * @param payment The ESConfig object containing payment service configuration.
     * @throws ExternalServiceException If the payment service returns an unexpected response.
     * @throws IOException              If an I/O error occurs while connecting to the service.
     */
    public ESPayment(ESConfig payment) throws ExternalServiceException, IOException {
        super(payment.getName(), payment.getURL(), payment.getResponseTime());
        handshake();
    }

    @Override
    public int makePurchase(JSONObject userDetails, JSONObject storeDetails, double price) throws Exception {
        try {
            String cardNumber = userDetails.getString("cardNumber");
            String month = userDetails.getString("month");
            String year = userDetails.getString("year");
            String holder = userDetails.getString("holder");
            String ccv = userDetails.getString("ccv");
            String id = userDetails.getString("id");
            Request paymentRequest = new PaymentRequest(cardNumber, month, year, holder, ccv, id);
            return sendRequest(paymentRequest);
        } catch (Exception e) {
            throw new Exception("Payment: " + e.getMessage());
        }
    }

    @Override
    public void cancelPurchase(String transactionId) throws Exception {
        Request cancelPayRequest = new CancelRequest("cancel_pay", transactionId);
        try {
            int result = sendRequest(cancelPayRequest);
            if (result == FAILED) {
                throw new Exception("Failed to cancel the payment transaction!");
            }
        } catch (Exception e) {
            throw new Exception("Payment: " + e.getMessage());
        }
    }
}
