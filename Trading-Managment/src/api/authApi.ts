import { noAuthApiClient } from './apiClient';
import { apiErrorHandlerWrapper } from './util';
import { EnterGuestResponseData, RegisterResponseData, TokenResponseBody } from '../types/responseTypes/authTypes';
import { LoginPostData, RegisterPostData } from '../types/requestTypes/authTypes';
import { ApiResponse } from '../types/apiTypes';



//apun connecting to the backend, the backend will return a token
export const authApi = {
    login: (credentials: LoginPostData): Promise<ApiResponse<TokenResponseBody>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/login', credentials)),
    register: (credentials: RegisterPostData): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/register', credentials)),
    logout: (credentials: number): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/logout', credentials)),
    guestEnter: (): Promise<ApiResponse<number>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/guest/enter')),
    ping: (credential: number): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/ping', credential)),

}