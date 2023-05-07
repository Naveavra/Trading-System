import { ApiResponseListData, ApiResponse } from "../types/apiTypes";
import { DeleteStoreParams, GetStoresParams, PatchStoreParams, PostStoreParams } from "../types/requestTypes/storeTypes";
import { Store } from "../types/systemTypes/Store";
import { noAuthApiClient, getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const productsApi = 
{
    getProducts: (params: GetStoresParams) :Promise<ApiResponseListData<Store>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/stores', {
            params: params
        })),
        
    postProduct: (params: PostStoreParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/stores', params)),

    patchProduct: (params: PatchStoreParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch('api/stores', params)),

    deleteProduct: (params: DeleteStoreParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().delete('api/stores', {
            params: params
        }))
    
}   