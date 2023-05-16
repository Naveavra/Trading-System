package service.payment;

public class GooglePay implements PaymentAdapter{

    @Override
    public void makePurchase(String accountNumber, double amount) throws Exception {
        throw new IllegalArgumentException("Loss Connection");
    }
}
