package service.ExternalService.Payment;

import org.json.JSONObject;
import server.Config.ESConfig;
import service.ExternalService.Supplier.ESSupply;
import service.ExternalService.Supplier.OurSupplyService;
import utils.Exceptions.ExternalServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The ProxyPayment class acts as a proxy for different payment services,
 * allowing dynamic selection and interaction with the chosen payment service.
 */
public class ProxyPayment implements PaymentAdapter {
    private PaymentAdapter real;

    private final Map<String, PaymentAdapter> paymentServices;


    /**
     * Constructs a ProxyPayment instance with an initial payment service.
     *
     * @throws Exception If an error occurs during initialization.
     */
    public ProxyPayment() throws Exception {
        this.paymentServices = new HashMap<>();
        loadConfig(new ESConfig());
    }

    /**
     * Constructs a ProxyPayment instance with an initial payment service.
     *
     * @param payment The configuration for the initial payment service.
     * @throws Exception If an error occurs during initialization.
     */
    public ProxyPayment(ESConfig payment) throws Exception {
        this.paymentServices = new HashMap<>();
        loadConfig(payment);
    }

    private void loadConfig(ESConfig payment) throws ExternalServiceException, IOException {
        OurPaymentService eli = new OurPaymentService();
        this.paymentServices.put(eli.getName(), eli);
        if (payment.isDefault())
        {
            this.real = eli;
        }
        else {
            ESPayment es = new ESPayment(payment);
            this.paymentServices.put(es.getName(), es);
            this.real = es;
        }
        this.real.setAvailable(true);
    }

    /**
     * Retrieves the available options for payment services.
     *
     * @return The list of available payment service names.
     */
    public List<String> getPaymentServicesAvailableOptions() {
        return paymentServices.entrySet()
                .stream()
                .filter(pay -> pay.getValue().isAvailable())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all the possible options for payment services, including both available and unavailable ones.
     *
     * @return The list of all payment service names.
     */
    public List<String> getPaymentServicesPossibleOptions() {
        return new ArrayList<>(paymentServices.keySet());
    }

    /**
     * Adds a new payment service to the available options.
     *
     * @param payment        The name of the payment service.
     * @param paymentAdapter The PaymentAdapter implementation for the service.
     * @throws Exception If the payment service already exists or an error occurs during addition.
     */
    public void addPaymentService(String payment, PaymentAdapter paymentAdapter) throws Exception {
        if (paymentServices.containsKey(payment)) {
            throw new IllegalArgumentException("This payment service: " + payment + " already exists in the possible payment services!");
        } else {
            paymentServices.put(payment, paymentAdapter);
            paymentAdapter.setAvailable(true);
        }
    }

    /**
     * Adds a payment service to the available options based on its name.
     *
     * @param paymentService The name of the payment service to add.
     * @throws Exception If the payment service doesn't exist or is already available.
     */
    public void addPaymentService(String paymentService) throws Exception {
        if (!paymentServices.containsKey(paymentService)) {
            throw new IllegalArgumentException("This payment service: " + paymentService + " doesn't exist in the possible payment services!");
        } else if (paymentServices.get(paymentService).isAvailable()) {
            throw new IllegalArgumentException("This payment service: " + paymentService + " is already available!");
        } else {
            paymentServices.get(paymentService).setAvailable(true);
        }
    }

    /**
     * Removes a payment service from the available options.
     *
     * @param paymentService The name of the payment service to remove.
     * @throws Exception If there is only one payment service available or the payment service doesn't exist or is already unavailable.
     */
    public void removePaymentService(String paymentService) throws Exception {
        if (getPaymentServicesAvailableOptions().size() <= 1) {
            throw new IllegalArgumentException("Can't remove payment service. Need at least 1 payment service available.");
        } else if (!paymentServices.containsKey(paymentService) || !paymentServices.get(paymentService).isAvailable()) {
            throw new IllegalArgumentException("This payment service: " + paymentService + " doesn't exist or is already unavailable!");
        } else {
            paymentServices.get(paymentService).setAvailable(false);
        }
    }

    /**
     * Sets the current active payment service.
     *
     * @param paymentAdapter The name of the payment service to set as active.
     * @throws Exception If the payment service doesn't exist or is unavailable.
     */
    public void setRealPayment(String paymentAdapter) throws Exception {
        if (paymentServices.containsKey(paymentAdapter) && paymentServices.get(paymentAdapter).isAvailable()) {
            real = paymentServices.get(paymentAdapter);
        } else {
            throw new Exception("The payment service: " + paymentAdapter + " doesn't exist or is unavailable!");
        }
    }

    @Override
    public boolean isAvailable() {
        return real != null;
    }

    private String getPaymentService(JSONObject paymentContent) throws Exception {
        try{
            String service_name = paymentContent.getString("payment_service");
            return service_name;
        }
        catch (Exception e)
        {
            throw new Exception("The payment doesn't exists!");
        }
    }

    @Override
    public int makePurchase(JSONObject userDetails, JSONObject storeDetails, double price) throws Exception {
        String payment = getPaymentService(userDetails);
        if (paymentServices.containsKey(payment) && paymentServices.get(payment).isAvailable()) {
            return paymentServices.get(payment).makePurchase(userDetails, storeDetails, price);
        }
        throw new Exception("The payment service: " + payment + " doesn't exist or is unavailable!");
    }

    @Override
    public void cancelPurchase(String transactionId) throws Exception {
        if (real != null) {
            real.cancelPurchase(transactionId);
        }
    }

    @Override
    public void setAvailable(boolean available) {

    }
}
