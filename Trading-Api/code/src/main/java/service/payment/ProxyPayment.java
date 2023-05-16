package service.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProxyPayment implements PaymentAdapter {

    private PaymentAdapter real = null;
    private HashMap<String, PaymentAdapter> paymentServices;

    private List<String> availablePaymentServices;
    //TODO: check by the requirement private HashMap<Integer, String> availablePaymentServices;

    public ProxyPayment(String real) {
        this.paymentServices = new HashMap<>();
        this.availablePaymentServices = new ArrayList<>();
        this.paymentServices.put("Google Pay", new GooglePay());
        this.paymentServices.put("Apple Pay", new ApplePay());
        this.availablePaymentServices.add(real);
        this.real = paymentServices.get(real);
    }

    public List<String> getPaymentServicesOptions()
    {
        return availablePaymentServices;
    }

    public void addPaymentService(String paymentService) throws Exception
    {
        if (!availablePaymentServices.contains(paymentService))
        {
            availablePaymentServices.add(paymentService);
        }
        else{
            throw new IllegalArgumentException("This payment service:" + paymentService + "already exists!!!");
        }
    }

    public void removePaymentService(String paymentService) throws Exception
    {
        if (availablePaymentServices.size() > 1)
        {
            throw new IllegalArgumentException("Can't remove payment service need at least 1 payment service!");
        }
        else if (!availablePaymentServices.contains(paymentService))
        {
            throw new IllegalArgumentException("This payment service:" + paymentService + " doesn't exists!!!");
        }
        else
        {
            availablePaymentServices.remove(paymentService);
        }
    }


    public void setRealPayment(String paymentAdapter){
        if(availablePaymentServices.contains(paymentAdapter))
            real = paymentServices.get(paymentAdapter);
    }

    @Override
    public void makePurchase(String accountNumber , double amount) throws Exception{
        if (real != null){
            real.makePurchase(accountNumber, amount);
        }
    }
}
