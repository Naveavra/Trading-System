package service.payment;

public class ApplePay implements PaymentAdapter{

    @Override
    public void makePurchase(String accountNumber, double amount) throws Exception {
        throw new IllegalArgumentException("Loss Connection");
    }
}
