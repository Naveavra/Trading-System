import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { GetProductsParams, PostProductsParams } from "../types/requestTypes/GetProductsParams";
import { Product } from "../types/systemTypes/Product";
import { getApiClient, noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";



export const productsApi = 
{
    getProducts: (params: Partial<GetProductsParams>) :Promise<ApiResponseListData<Product>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/products', {
            params: params
        })),
    postProduct: (params: Partial<PostProductsParams>) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/products', params)),
    
}   