import { ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { addAdminParams, answerComplaintParams, cancelMembershipParams, closeStorePerminentlyParams } from "../types/requestTypes/adminTypes";
import { Complaint } from "../types/systemTypes/Complaint";
import { LogRecord } from "../types/systemTypes/Log";
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

    cleanUser: (userId: number): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/removeUser`, { userId: userId })),


}