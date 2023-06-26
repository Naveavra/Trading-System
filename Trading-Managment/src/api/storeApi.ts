import { ApiResponseListData, ApiResponse } from "../types/apiTypes";
import { AnswerQuestionParams, AppointUserParams, DeleteStoreParams, GetStoreProducts, GetStoresParams, PatchStoreParams, PostStoreParams, WaitingAppointmentParams, WriteQuestionParams, WriteReviewOnProductParams, WriteReviewParams, fireUserParams, getAppointmentsHistoryParams, patchPermissionsParams } from "../types/requestTypes/storeTypes";
import { Product } from "../types/systemTypes/Product";
import { Store } from "../types/systemTypes/Store";
import { StoreInfo } from "../types/systemTypes/StoreInfo";
import { noAuthApiClient, getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const storeApi =
{
    getStoresInfo: (): Promise<ApiResponseListData<StoreInfo>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get('api/stores/info', { headers: { 'Content-Type': 'application/json' } })),

    getStore: (params: GetStoresParams): Promise<ApiResponse<Store>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/${params.storeId}/getStore`, params)),

    getProducts: (params: GetStoreProducts): Promise<ApiResponseListData<Product>> =>
        apiErrorHandlerWrapper(noAuthApiClient.get(`api/stores/${params.storeId}/products`, { params: params })),

    postStore: (params: PostStoreParams): Promise<ApiResponse<number>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/stores', params)),

    patchStore: (params: PatchStoreParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch(`api/stores/${params.storeId}`, params)),

    deleteStore: (params: DeleteStoreParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().delete(`api/stores/${params.storeId}`, {
            params: params
        })),

    appointOwner: (params: AppointUserParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/${params.storeId}/appointments/owners`, params)),

    appointManager: (params: AppointUserParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/${params.storeId}/appointments/managers`, params)),

    fireManager: (params: fireUserParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch(`api/stores/${params.storeId}/appointments/managers`, params)),

    fireOwner: (params: fireUserParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch(`api/stores/${params.storeId}/appointments/owners`, params)),

    patchPermissions: (params: patchPermissionsParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().patch(`api/stores/${params.storeId}/permissions`, params)),

    amswerQuestion: (params: AnswerQuestionParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/${params.storeId}/questions/answers`, params)),

    writeReview: (params: WriteReviewParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/${params.storeName}/reviews/write`, params)),

    writeQuestion: (params: WriteQuestionParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/${params.storeId}/questions/write`, params)),

    writeReviewOnProduct: (params: WriteReviewOnProductParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/reviewOnProduct`, params)),

    answerOnWaitingAppointment: (params: WaitingAppointmentParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/stores/${params.storeId}/appointments/answer`, params)),
}   