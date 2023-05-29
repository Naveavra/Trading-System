import { ApiResponseListData } from "../types/apiTypes";
import { LogRecord } from "../types/systemTypes/Log";
import { getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const adminApi = {
    getLogger: (adminId: number): Promise<ApiResponseListData<LogRecord>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/admin/logger/${adminId}`, { adminId: adminId })),
}