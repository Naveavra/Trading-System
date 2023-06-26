import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { postRegularDicountParams, postCompositeDicountParams, deleteDiscountParams } from "../types/requestTypes/discountTypes";
import { Discount } from "../types/systemTypes/Discount";
import { getApiClient, noAuthApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const discountApi = {
    getDiscounts: (): Promise<ApiResponseListData<Discount>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/discounts/`, {})),
    //maybe this should be placed in storeApi
    getDiscountsByStore: (storeId: number): Promise<ApiResponseListData<Discount>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/discounts/${storeId}`, {})),

    postRegularDiscount: (discount: postRegularDicountParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/discounts/regular`, discount)),

    postCompositeDiscount: (discount: postCompositeDicountParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/discounts/composite`, discount)),

    deleteDiscount: (discount: deleteDiscountParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/discounts/delete`, discount))
}