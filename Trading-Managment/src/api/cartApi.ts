import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { DeleteCartParams, GetCartParams, PatchCartParams, PostBasketParams } from "../types/requestTypes/cartTypes";
import { Basket } from "../types/systemTypes/Basket";
import {  noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";


export const cartApi = 
{
    getCart: (params: GetCartParams) : Promise<ApiResponseListData<Basket>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/cart/${params.userId}`, {
            params: params
        })),

    patchCart: (params: PatchCartParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.patch(`api/cart/${params.userId}`, params)),

    deleteCart: (params: DeleteCartParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.delete(`api/cart/${params.userId}`, {
            params: params
        })),

    postBasket: (params: PostBasketParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post(`api/cart/${params.userId}`, params)),
    
}