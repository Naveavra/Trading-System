// import { PanoramaSharp } from "@mui/icons-material";
import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { DeleteProductsParams, GetStoreProductsParams, PatchProductsParams, PostProductsParams } from "../types/requestTypes/GetProductsParams";
import { ProductResponseData } from "../types/responseTypes/productTypes";
import { Product } from "../types/systemTypes/Product";
import { getApiClient, noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";



export const productsApi = 
{
    getProducts: (params: Partial<GetStoreProductsParams>) :Promise<ApiResponseListData<Product>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/products', {
            params: params
        })),
        
    postProduct: (params: PostProductsParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/products', params)),

    patchProduct: (params: PatchProductsParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch('api/products', params)),

    deleteProduct: (params: DeleteProductsParams) : Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().delete('api/products', {
            params: params
        }))
    
    
    
}   