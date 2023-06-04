package service.ExternalService;

import service.ExternalService.Request;

public class CancelRequest extends Request {
    protected CancelRequest(String actionType, String transactionId) {
        super(actionType, "&transaction_id=" + transactionId);
    }

}
