package service.ExternalService;

import service.ExternalService.Request;

public class CancelRequest extends Request {
    public CancelRequest(String actionType, String transactionId) {
        super(actionType, "&transaction_id=" + transactionId);
    }

}
