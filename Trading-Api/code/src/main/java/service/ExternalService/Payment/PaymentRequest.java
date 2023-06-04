package service.ExternalService.Payment;

import service.ExternalService.Request;

public class PaymentRequest extends Request {
    private String cardNumber;
    private String month;
    private String year;
    private String holder;
    private String ccv;
    private String id;

    public PaymentRequest(String cardNumber, String month, String year, String holder, String ccv, String id) {
        super("pay");
        setAdditional_parameters(makeParamsToString(cardNumber, month, year, holder, ccv, id));
    }

    private String makeParamsToString(String cardNumber, String month, String year, String holder, String ccv, String id)
    {
        String additionalParameters =
                "&card_number=" + cardNumber +
                        "&month=" + month +
                        "&year=" + year +
                        "&holder=" + holder +
                        "&ccv=" + ccv +
                        "&id=" + id;
        return additionalParameters;
    }
}
