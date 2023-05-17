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

    public List<String> getPaymentServicesAvailableOptions()
    {
        return availablePaymentServices;
    }

    public List<String> getPaymentServicesPossibleOptions()
    {
        return paymentServices.keySet().stream().toList();
    }

    public void addPaymentService(String paymentService) throws Exception
    {
        if (availablePaymentServices.contains(paymentService))
        {
            throw new Exception("This payment service:" + paymentService + "already exists!!!");

        }
        else if(!paymentServices.containsKey(paymentService))
        {
            throw new Exception("This payment service:" + paymentService + "doesn't exists in the possible payment services!!!");
        }
        else{
            availablePaymentServices.add(paymentService);
        }
    }

    public void removePaymentService(String paymentService) throws Exception
    {
        if (availablePaymentServices.size() > 1)
        {
            throw new Exception("Can't remove payment service need at least 1 payment service!");
        }
        else if (!availablePaymentServices.contains(paymentService))
        {
            throw new Exception("This payment service:" + paymentService + " doesn't exists!!!");
        }
        else
        {
            availablePaymentServices.remove(paymentService);
        }
    }


    public void setRealPayment(String paymentAdapter) throws Exception{
        if(availablePaymentServices.contains(paymentAdapter))
            real = paymentServices.get(paymentAdapter);
        else{
            throw new Exception("The " + paymentAdapter + "doesn't exist!");
        }
    }

    @Override
    public void makePurchase(String accountNumber , double amount) throws Exception{
        if (real != null){
            real.makePurchase(accountNumber, amount);
        }
    }
}
