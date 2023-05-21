import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { DeleteCartParams, GetCartParams, PatchCartParams, PostBasketParams, buyCartParams } from "../types/requestTypes/cartTypes";
import { Basket } from "../types/systemTypes/Basket";
import { Product } from "../types/systemTypes/Product";
import { noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";


export const cartApi =
{
    getCart: (params: GetCartParams): Promise<ApiResponseListData<Product>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post(`api/cart/${params.userId}`, params)),

    patchCart: (params: PatchCartParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.patch(`api/cart/${params.userId}`, params)),

    deleteCart: (params: DeleteCartParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.delete(`api/cart/${params.userId}`, {
            params: params
        })),
    // postBaket: (params: PostBasketParams): Promise<ApiResponse<string>> =>
    //     apiErrorHandlerWrapper(noAuthApiClient.post(`api/cart/${params.storeId}`, params)),
    addToCart: (params: PatchCartParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.patch(`api/cart/add/${params.userId}`, params)),

    removeFromCart: (params: PatchCartParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.patch(`api/cart/remove/${params.userId}`, params)),

    buyCart: (params: buyCartParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post(`api/cart/buy/${params.userId}`, params))
}