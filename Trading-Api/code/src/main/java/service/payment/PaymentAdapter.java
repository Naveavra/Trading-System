package service.payment;

public interface PaymentAdapter {

    public void makePurchase(String accountNumber , double amount) throws Exception;
}
