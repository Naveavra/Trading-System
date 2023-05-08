import { ApiResponseListData, ApiResponse } from "../types/apiTypes";
import { DeleteStoreParams, GetStoreProducts, GetStoresParams, PatchStoreParams, PostStoreParams } from "../types/requestTypes/storeTypes";
import { Product } from "../types/systemTypes/Product";
import { Store } from "../types/systemTypes/Store";
import { noAuthApiClient, getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const storeApi = 
{
    getStores: (params: GetStoresParams) :Promise<ApiResponseListData<Store>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/stores', {
            params: params
        })),
        
    postStore: (params: PostStoreParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/stores', params)),

    patchStore: (params: PatchStoreParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch('api/stores', params)),

    deleteStore: (params: DeleteStoreParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().delete('api/stores', {
            params: params
        })),

    getProducts: (params: GetStoreProducts) : Promise<ApiResponseListData<Product>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/products',{ params: params }))

}   