import { getApiClient, noAuthApiClient } from './apiClient';
import { apiErrorHandlerWrapper } from './util';
import { EnterGuestResponseData, RegisterResponseData, TokenResponseBody, getClientNotifications, getClientResponseData } from '../types/responseTypes/authTypes';
import { LoginPostData, RegisterPostData, getUserData, getUserNotifications, messageParams } from '../types/requestTypes/authTypes';
import { ApiResponse, ApiResponseListData } from '../types/apiTypes';
import { MyNotification } from '../types/systemTypes/Notification';



//apun connecting to the backend, the backend will return a token
export const authApi = {
    //done
    login: (credentials: LoginPostData): Promise<ApiResponse<TokenResponseBody>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/login', credentials)),
    register: (credentials: RegisterPostData): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/register', credentials)),
    logout: (credentials: number): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/auth/logout', { userId: credentials })),
    guestEnter: (): Promise<ApiResponse<number>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/guest/enter')),
    ping: (credential: number): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/ping', { userId: credential })),
    getNotifications: (credentials: getUserNotifications): Promise<ApiResponse<MyNotification>> =>
        apiErrorHandlerWrapper(getApiClient().post(`api/auth/notifications`, { userId: credentials.userId, token: credentials.token })),
    getClient: (credentials: getUserData): Promise<ApiResponse<getClientResponseData>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/auth/getClient', { userId: credentials.userId })),

    sendMessage: (credentials: messageParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/auth/notifications/msg', credentials)),

}