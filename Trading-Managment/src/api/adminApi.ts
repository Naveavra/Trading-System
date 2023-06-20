import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { addAdminParams, answerComplaintParams, cancelMembershipParams, closeStorePerminentlyParams, updateServiceParams } from "../types/requestTypes/adminTypes";
import { Complaint } from "../types/systemTypes/Complaint";
import { LogRecord } from "../types/systemTypes/Log";
import { SystemStatus } from "../types/systemTypes/SystemStatus";
import { getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const adminApi = {
    getLogger: (adminId: number): Promise<ApiResponseListData<LogRecord>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/logger/${adminId}`, { adminId: adminId })),

    appointAdmin: (params: addAdminParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/add`, params)),

    getComplaints: (adminId: number): Promise<ApiResponseListData<Complaint>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/complaints/${adminId}`, { adminId: adminId })),

    answerCompolaint: (credentials: answerComplaintParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/complaints/answer`, credentials)),

    adminResign: (credentials: number): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/remove`, { adminId: credentials })),

    closeStorePerminently: (credentials: closeStorePerminentlyParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/closeStorePermanently`, credentials)),

    cancelMembership: (credentials: cancelMembershipParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/cancelMembership`, credentials)),

    cleanUser: (userName: string): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/removeUser`, { userName: userName })),

    marketStatus: (credentials: number): Promise<ApiResponse<SystemStatus>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/admin/marketStatus', { userId: credentials })),

    updateService: (credentials: updateServiceParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/admin/updateService', credentials)),

    getAdminPaymenysService: (credentials: number): Promise<ApiResponseListData<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/admin/services/payments', { adminId: credentials })),

    getAdminSupplyService: (credentials: number): Promise<ApiResponseListData<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/admin/services/suppliers', { adminId: credentials })),
}