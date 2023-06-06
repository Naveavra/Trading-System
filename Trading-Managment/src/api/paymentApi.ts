import { ApiResponseListData } from "../types/apiTypes";
import { noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const paymentApi = {
    getSuppliers: (): Promise<ApiResponseListData<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/services/suppliers`)),
    getPaymentsService: (): Promise<ApiResponseListData<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/services/payments`)),
}