package service.ExternalService.Payment;

import org.json.JSONObject;

/**
 * The PaymentAdapter interface defines the contract for interacting with a payment service.
 */
public interface PaymentAdapter {
    /**
     * Checks if the payment service is available.
     *
     * @return `true` if the payment service is available, `false` otherwise.
     */
    boolean isAvailable();

    /**
     * Makes a purchase with the payment service.
     *
     * @param userDetails  The JSON object containing the user payment details.
     * @param storeDetails  The JSON object containing the store payment details.
     * @param price The price of the purchase.
     * @return The transaction ID or order ID associated with the purchase.
     * @throws Exception If an error occurs during the purchase.
     */
    int makePurchase(JSONObject userDetails, JSONObject storeDetails, double price) throws Exception;

    /**
     * Cancels a purchase transaction with the payment service.
     *
     * @param transactionId The transaction ID or order ID to cancel.
     * @throws Exception If an error occurs during the cancellation.
     */
    void cancelPurchase(String transactionId) throws Exception;

    /**
     * Sets the availability of the payment service.
     *
     * @param available `true` to set the payment service as available, `false` otherwise.
     */
    void setAvailable(boolean available);
}
