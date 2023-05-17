import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import {getOrderParams, postOrderParams } from "../types/requestTypes/orderTypes";
import { OrderInfo } from "../types/systemTypes/OrderInfo";
import { getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const orderApi =
{
    getOrder: (params: getOrderParams): Promise<ApiResponseListData<OrderInfo>> =>
        apiErrorHandlerWrapper(getApiClient().get('api/orders', {params : params})),

    postOrder: (params: postOrderParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/orders', params)),


}   