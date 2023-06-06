package service.ExternalService.Payment;

import org.json.JSONObject;
import service.ExternalService.Supplier.SupplierAdapter;
import service.ExternalService.WSEPService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ProxyPayment implements PaymentAdapter {

    private PaymentAdapter real = null;
    private HashMap<String, PaymentAdapter> paymentServices;


    public ProxyPayment() throws Exception {
        this.paymentServices = new HashMap<>();
        this.paymentServices.put("WSEP", new WSEPService());
        this.paymentServices.put("Mock", new PaymentAdapterMock());
        this.real = paymentServices.get("WSEP");
        this.real.setAvailable(true);
    }

    public List<String> getPaymentServicesAvailableOptions()
    {
        return paymentServices.entrySet()
                .stream()
                .filter(pay -> pay.getValue().isAvailable())
                .map(pay -> pay.getKey())
                .collect(Collectors.toList());
    }

    public List<String> getPaymentServicesPossibleOptions()
    {
        return paymentServices.keySet().stream().toList();
    }

    public void addPaymentService(String payment, PaymentAdapter paymentAdapter) throws Exception {
        if (paymentServices.containsKey(payment))
        {
            throw new Exception("This payment service:" + payment + "doesn't exists in the possible payment services!!!");
        }
        else{
            paymentServices.put(payment, paymentAdapter);
            paymentAdapter.setAvailable(true);
        }
    }

    public void addPaymentService(String paymentService) throws Exception
    {
        if (!paymentServices.containsKey(paymentService))
        {
            throw new Exception("This payment service:" + paymentService + "doesn't exists in the possible payment services!!!");
        }
        else if(paymentServices.get(paymentService).isAvailable())
        {
            throw new Exception("This payment service:" + paymentService + "already available exists!!!");
        }
        else{
            paymentServices.get(paymentService).setAvailable(true);
        }
    }



    public void removePaymentService(String paymentService) throws Exception
    {
        if (getPaymentServicesAvailableOptions().size() <= 1)
        {
            throw new IllegalArgumentException("Can't remove payment service need at least 1 supplier service!");
        }
        else if (!paymentServices.containsKey(paymentService) || !paymentServices.get(paymentService).isAvailable())
        {
            throw new IllegalArgumentException("This payment service:" + paymentService + " doesn't available or exists!!!");
        }
        else
        {
            paymentServices.get(paymentService).setAvailable(false);
        }
    }


    public void setRealPayment(String paymentAdapter) throws Exception{
        if(paymentServices.containsKey(paymentAdapter) &&
                paymentServices.get(paymentAdapter).isAvailable()) {
            real = paymentServices.get(paymentAdapter);
        }
        else{
            throw new Exception("The " + paymentAdapter + "doesn't available or exist!");
        }
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void setAvailable(boolean available) {

    }

    @Override
    public void makePurchase(String accountNumber , double amount) throws Exception{
        if (real != null){
            real.makePurchase(accountNumber, amount);
        }
    }

    @Override
    public int makePurchase(JSONObject paymentContent, double price) throws Exception {
        String payment = paymentContent.getString("payment_service");
        if(paymentServices.containsKey(payment) &&
                paymentServices.get(payment).isAvailable()) {
            return paymentServices.get(payment).makePurchase(paymentContent, price);
        }
        throw new Exception("The " + payment + "doesn't available or exist!");
    }

    @Override
    public void cancelPurchase(String transactionId) throws Exception {
        if (real != null){
            real.cancelPurchase(transactionId);
        }
    }
}
