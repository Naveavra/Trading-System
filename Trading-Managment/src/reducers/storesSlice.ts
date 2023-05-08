import {createAsyncThunk } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse, ApiResponseListData } from "../types/apiTypes";

import { storeApi } from "../api/storeApi";
import { DeleteStoreParams, GetStoresParams, PatchStoreParams, PostStoreParams } from "../types/requestTypes/storeTypes";
import { Store } from "../types/systemTypes/Store";

const reducerName = 'stores';

interface StoreState {
    storeState: {
        isLoading: boolean;
        responseData?: Store;
        error?: ApiError;
        watchedStore?: Store;
    },
    isLoading: boolean;
    responseData?: ApiListData<Store>;
    error?: ApiError;
};



const initialState: StoreState = {
    storeState: {
        isLoading: false,
        responseData: undefined,
        error: undefined,
        watchedStore: undefined,
    },
    isLoading: false,
    responseData: undefined,
    error: undefined,
};

export const postStore = createAsyncThunk<
    {responseBody: ApiResponse<string> },
    PostStoreParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/post',
    async (formData, thunkApi) =>{
        return storeApi.postStore(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponse<string>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

    export const patchStore = createAsyncThunk<
    {responseBody: ApiResponse<string> },
    PatchStoreParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/patch',
    async (formData, thunkApi) =>{
        return storeApi.patchStore(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponse<string>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const deleteStore = createAsyncThunk<
    {responseBody: ApiResponse<string> },
    DeleteStoreParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/delete',
    async (formData, thunkApi) =>{
        return storeApi.deleteStore(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponse<string>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const getStore = createAsyncThunk<
    {responseBody: ApiResponseListData<Store> },
    GetStoresParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/get',
    async (formData, thunkApi) =>{
        return storeApi.getStores(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponseListData<Store>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });