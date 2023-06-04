import { ApiResponseListData } from "../types/apiTypes";
import { noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";
import { Supplier, paymentService } from "../types/systemTypes/Supplier";

export const paymentApi = {
    getSuppliers: (): Promise<ApiResponseListData<Supplier>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/payment/services/suppliers`)),
    getPaymentsService: (): Promise<ApiResponseListData<paymentService>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/payment/services/payments`)),
}