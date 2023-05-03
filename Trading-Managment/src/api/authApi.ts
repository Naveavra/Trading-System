import { noAuthApiClient } from './apiClient';
import { apiErrorHandlerWrapper } from './util';
import { RegisterResponseData, TokenResponseBody } from '../types/responseTypes/authTypes';
import { LoginPostData, RegisterPostData } from '../types/requestTypes/authTypes';



//apun connecting to the backend, the backend will return a token
export const authApi = {
    login: (credentials: LoginPostData): Promise<TokenResponseBody> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/login', credentials)),
    register: (credentials: RegisterPostData): Promise<RegisterResponseData> =>
        apiErrorHandlerWrapper(noAuthApiClient.post('api/auth/register/', credentials)),
}