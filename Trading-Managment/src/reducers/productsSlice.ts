import { createAsyncThunk } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse, ApiResponseListData, ValidationError } from "../types/apiTypes";
import { Product } from "../types/systemTypes/Product";
import { ProductResponseData } from "../types/responseTypes/productTypes";
import { DeleteProductsParams, GetProductsParams, PatchProductsParams, PostProductsParams } from "../types/requestTypes/GetProductsParams";
import { productsApi } from "../api/productsApi";

const reducerName = 'products';

interface ProductsState {
    productState: {
        isLoading: boolean;
        responseData?: Product;
        error?: ApiError | ValidationError;
        watchedProduct?: Product;
    },
    isLoading: boolean;
    responseData?: ApiListData<Product>;
    error?: ApiError;
};



const initialState: ProductsState = {
    productState: {
        isLoading: false,
        responseData: undefined,
        error: undefined,
        watchedProduct: undefined,
    },
    isLoading: false,
    responseData: undefined,
    error: undefined,
};

export const postProduct = createAsyncThunk<
    {responseBody: ApiResponse<string> },
    PostProductsParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/post',
    async (formData, thunkApi) =>{
        return productsApi.postProduct(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponse<string>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

    export const patchProduct = createAsyncThunk<
    {responseBody: ApiResponse<string> },
    PatchProductsParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/patch',
    async (formData, thunkApi) =>{
        return productsApi.patchProduct(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponse<string>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const deleteProduct = createAsyncThunk<
    {responseBody: ApiResponse<string> },
    DeleteProductsParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/delete',
    async (formData, thunkApi) =>{
        return productsApi.deleteProduct(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponse<string>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const getProduct = createAsyncThunk<
    {responseBody: ApiResponseListData<Product> },
    GetProductsParams,
    {rejectValue: ApiError}
>(
    '${reducerName}/get',
    async (formData, thunkApi) =>{
        return productsApi.getProduct(formData)
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiResponse<string>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    }); //should fix product 


