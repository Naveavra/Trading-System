import axios, { AxiosResponse, CancelToken } from 'axios';
import { backendUrl } from '..//config';
import { store } from '../redux/store';

export const noAuthApiClient = axios.create({
    baseURL: backendUrl,
});
export const getApiClient = () => (axios.create({
    baseURL: backendUrl,
    headers: {
        Authorization: 'Token ' + store.getState().auth.token ?? ''
    }
}));