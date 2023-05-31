import { Payment } from "../types/systemTypes/externalService";
import { ApiResponse } from "../types/apiTypes";
import { CancelPayParams, HandShakeParams, PayParams } from "../types/requestTypes/paymentTypes";
import { ExternalClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const paymentApi = {

    handshake: (): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(ExternalClient().post('', { action_type: Payment.HANDSHAKE })),
    pay: (params: PayParams): Promise<ApiResponse<number>> =>
        apiErrorHandlerWrapper(ExternalClient().post('', { action_type: Payment.PAY, ...params })),
    cancelPay: (params: CancelPayParams): Promise<ApiResponse<number>> =>
        apiErrorHandlerWrapper(ExternalClient().post('', { action_type: Payment.CANCEL_PAY, ...params })),
}