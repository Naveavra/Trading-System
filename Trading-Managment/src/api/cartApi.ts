import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { DeleteCartParams, GetCartParams, PatchCartParams, PostBasketParams } from "../types/requestTypes/cartTypes";
import { Cart } from "../types/systemTypes/Cart";
import { noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";


export const cartApi =
{
    getCart: (params: GetCartParams): Promise<ApiResponse<Cart>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/cart/${params.userId}`, {
            params: params
        })),

    patchCart: (params: PatchCartParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.patch(`api/cart/${params.userId}`, params)),

    deleteCart: (params: DeleteCartParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.delete(`api/cart/${params.userId}`, {
            params: params
        })),
    // postBaket: (params: PostBasketParams): Promise<ApiResponse<string>> =>
    //     apiErrorHandlerWrapper(noAuthApiClient.post(`api/cart/${params.storeId}`, params)),



}