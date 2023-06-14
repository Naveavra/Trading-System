import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { GetStoreShoppingRulesParams, PostShoppingRuleParams, deleteShoppingRuleParams } from "../types/requestTypes/ShoppingRule";
import { getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const shoppingRuleApi = {
    postShoppingRule: (params: PostShoppingRuleParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/shoppingRule', params)),
    deleteShoppingRule: (params: deleteShoppingRuleParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().delete('api/shoppingRule', { params: params })),
}