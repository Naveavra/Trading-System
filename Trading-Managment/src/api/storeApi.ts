import { ApiResponseListData, ApiResponse } from "../types/apiTypes";
import { DeleteStoreParams, GetStoreProducts, GetStoresParams, PatchStoreParams, PostStoreParams } from "../types/requestTypes/storeTypes";
import { Product } from "../types/systemTypes/Product";
import { Store } from "../types/systemTypes/Store";
import { StoreInfo } from "../types/systemTypes/StoreInfo";
import { noAuthApiClient, getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const storeApi =
{
    getStoresInfo: (): Promise<ApiResponseListData<StoreInfo>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/stores/info')),

    getProducts: (params: GetStoreProducts): Promise<ApiResponseListData<Product>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/products', { params: params })),

    postStore: (params: PostStoreParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/stores', params)),

    patchStore: (params: PatchStoreParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch(`api/stores/${params.storeId}`, params)),

    deleteStore: (params: DeleteStoreParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().delete(`api/stores/${params.storeId}`, {
            params: params
        })),
    getStore: (params: GetStoresParams): Promise<ApiResponse<Store>> =>
        apiErrorHandlerWrapper(getApiClient().get('api/stores', { params: params })),
    
}   