// import { PanoramaSharp } from "@mui/icons-material";
import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { DeleteProductsParams, GetStoreProductsParams, PatchProductsParams, PostProductsParams } from "../types/requestTypes/productTypes";
import { Product } from "../types/systemTypes/Product";
import { getApiClient, noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";



export const productsApi =
{
    getProducts: (): Promise<ApiResponseListData<Product>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/products')),

    postProduct: (params: PostProductsParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/products', params)),

    patchProduct: (params: Partial<PatchProductsParams>): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch('api/products', params)),

    deleteProduct: (params: DeleteProductsParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().delete('api/products', {
            params: params
        }))
}   