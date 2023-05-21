import { ApiResponse } from "../types/apiTypes";
import { CancelSupplyParams, SupplyParams } from "../types/requestTypes/supplyTypes";
import { Supply } from "../types/systemTypes/externalService";
import { ExternalClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const supplierApi = {
    supply: (params: SupplyParams): Promise<ApiResponse<number>> =>
        apiErrorHandlerWrapper(ExternalClient().post('', { action_type: Supply.SUPPLY, ...params })),
    caches: (params: CancelSupplyParams): Promise<ApiResponse<number>> =>
        apiErrorHandlerWrapper(ExternalClient().post('', { action_type: Supply.CANCEL_SUPPLY, ...params })),
}