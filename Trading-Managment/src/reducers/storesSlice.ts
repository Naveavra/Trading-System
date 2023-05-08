import {createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse, ApiResponseListData } from "../types/apiTypes";

import { storeApi } from "../api/storeApi";
import { DeleteStoreParams, GetStoresParams, PatchStoreParams, PostStoreParams } from "../types/requestTypes/storeTypes";
import { Store, emptyStore } from "../types/systemTypes/Store";
import { Palette } from "@mui/icons-material";

const reducerName = 'stores';

interface StoreState {
    storeState: {
        isLoading: boolean;
        responseData?: ApiResponse<string>;
        error: string | null;
        watchedStore?: Store;
    },
    isLoading: boolean;
    responseData: ApiListData<Store>;
    error: string | null ;
};



const initialState: StoreState = {
    storeState: {
        isLoading: false,
        responseData: "",
        error: null,
        watchedStore: emptyStore,
    },
    isLoading: false,
    responseData: {data:{results:[]}},
    error: null,
};

export const postStore = createAsyncThunk<
    {responseBody: ApiResponse<string> },
    PostStoreParams,
    {rejectValue: ApiError}
>(
    `${reducerName}/post`,
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

export const getStores = createAsyncThunk<
    {responseBody: ApiListData<Store> },
    void,
    {rejectValue: ApiError}
>(
    `${reducerName}/get`,
    async (_,thunkApi) =>{
        return storeApi.getStores()
        .then((res) => thunkApi.fulfillWithValue({
            responseBody: res as ApiListData<Store>
        }))
        .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

    const { reducer: storeReducer, actions: storeActions } = createSlice({
        name: reducerName,
        initialState,
        reducers:{
            clearStoreError: (state) => {
                state.error = null;
            },
            clearStoreMsg: (state) => {
                state.storeState.error = null;
            },
            setWhatchedStore:(state , {payload}) =>{
                return {
                    ...state,
                        storeState:{
                            ...state.storeState,
                            watchedStore:state.responseData?.data.results.find((store)=>store.id === payload)
                        }
                }
            }
        },
        extraReducers: builder => {
            //getstore
            builder.addCase(getStores.pending, (state) => {
                state.isLoading = true;
                state.error = null;
            });
            builder.addCase(getStores.fulfilled, (state, { payload } ) =>{
                state.isLoading = false;
                console.log("response body", payload.responseBody);
                state.responseData = payload.responseBody;
                console.log("response data",state.responseData);
                state.error=null;
            });
            builder.addCase(getStores.rejected,(state,{payload})=>{
                state.isLoading=false;
                state.error = payload?.message.data.errorMsg ?? "error during get store";
            });
            //postStore
            builder.addCase(postStore.pending,(state)=>{
                state.storeState.isLoading = true;
                state.storeState.error = null;
            });
            builder.addCase(postStore.fulfilled,(state,{payload})=>{
                state.storeState.isLoading = false;
                state.storeState.responseData = payload.responseBody;
            });
            builder.addCase(postStore.rejected,(state,{payload})=>{
                state.storeState.error = payload?.message.data.errorMsg;
                state.storeState.isLoading =false;
            });
            //patchStore
            builder.addCase(patchStore.pending,(state)=>{
                state.storeState.isLoading = true;
                state.storeState.error = undefined;
            });
            builder.addCase(patchStore.fulfilled,(state,{payload})=>{
                state.storeState.isLoading = false;
                state.storeState.responseData = payload.responseBody;
            });
            builder.addCase(patchStore.rejected,(state,{payload})=>{
                state.storeState.error =payload;
                state.storeState.isLoading =false;
            });
            //delete store
            builder.addCase(deleteStore.pending,(state)=>{
                state.storeState.isLoading = true;
                state.storeState.error = undefined;
            });
            builder.addCase(deleteStore.fulfilled,(state,{payload})=>{
                state.storeState.isLoading = false;
                state.storeState.responseData = payload.responseBody;
            });
            builder.addCase(deleteStore.rejected,(state,{payload})=>{
                state.storeState.error =payload;
                state.storeState.isLoading =false;
            });
        }
    });
         

// Action creators are generated for each case reducer function
export const { clearStoreError, clearStoreMsg } = storeActions;
export default storeReducer;