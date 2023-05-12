import { Action, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse } from "../types/apiTypes";

import { storeApi } from "../api/storeApi";
import { AppointOwnerParams, DeleteStoreParams, GetStoresParams, PatchStoreParams, PostStoreParams } from "../types/requestTypes/storeTypes";
import { Store, emptyStore } from "../types/systemTypes/Store";
import { StoreInfo, emptyStoreInfo } from "../types/systemTypes/StoreInfo";

//TODO implement the getProducts method from the storeApi
const reducerName = 'stores';

interface StoreState {
    storeState: {
        isLoading: boolean;
        responseData: string | null;
        error: string | null;
        watchedStore: Store;
        wahtchedStoreInfo: StoreInfo;
    },
    isLoading: boolean;
    storeInfoResponseData: ApiListData<StoreInfo>;
    error: string | null;
};



const initialState: StoreState = {
    storeState: {
        isLoading: false,
        responseData: null,
        error: null,
        watchedStore: emptyStore,
        wahtchedStoreInfo: emptyStoreInfo,
    },
    isLoading: false,
    storeInfoResponseData: { data: { results: [] } },
    error: null,
};

export const postStore = createAsyncThunk<
    string,
    PostStoreParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/post`,
    async (formData, thunkApi) => {
        return storeApi.postStore(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const patchStore = createAsyncThunk<
    string,
    PatchStoreParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/patch`,
    async (formData, thunkApi) => {
        return storeApi.patchStore(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const deleteStore = createAsyncThunk<
    string,
    DeleteStoreParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/delete`,
    async (formData, thunkApi) => {
        return storeApi.deleteStore(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const getStoresInfo = createAsyncThunk<
    ApiListData<StoreInfo>,
    void,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (_, thunkApi) => {
        return storeApi.getStoresInfo()
            .then((res) => thunkApi.fulfillWithValue(res as ApiListData<StoreInfo>))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
//get store
export const getStore = createAsyncThunk<
    Store,
    GetStoresParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/getStore`,
    async (params, thunkApi) => {
        return storeApi.getStore(params)
            .then((res) => thunkApi.fulfillWithValue(res as Store))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
export const appointManager = createAsyncThunk<
   string,
    AppointOwnerParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/appointments/post`,
    async (params, thunkApi) => {
        return storeApi.appointManager(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
    //TODO complete the other functions as well

const { reducer: storesReducer, actions: storesActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearStoresError: (state, action: Action) => {
            state.error = null;
        },
        clearStoreError: (state, action) => {
            state.storeState.error = null;
        },
        setWhatchedStoreInfo: (state, action) => {
            state.storeState.wahtchedStoreInfo = state.storeInfoResponseData.data.results.find((storeInfo) => storeInfo.id === action.payload) ?? emptyStoreInfo;
        },
    },
    extraReducers: (builder) => {
        //getstores
        builder.addCase(getStoresInfo.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(getStoresInfo.fulfilled, (state, { payload }) => { //payload is what we get back from the function 
            state.isLoading = false;
            state.storeInfoResponseData = payload;
            state.error = null;
        });
        builder.addCase(getStoresInfo.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during getStores";
        });
        //postStore
        builder.addCase(postStore.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(postStore.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        builder.addCase(postStore.rejected, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.error = payload?.message.data ?? "error during postStore";
        });
        //patchStore
        builder.addCase(patchStore.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(patchStore.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        builder.addCase(patchStore.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during patchStore";
            state.storeState.isLoading = false;
        });
        //delete store
        builder.addCase(deleteStore.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(deleteStore.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        builder.addCase(deleteStore.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during deleteStore";
            state.storeState.isLoading = false;
        });
        //get store
        builder.addCase(getStore.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(getStore.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.watchedStore = payload;
        });
        //appointManager
        builder.addCase(appointManager.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(appointManager.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(appointManager.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
       
    }
});


// Action creators are generated for each case reducer function
export const { clearStoresError, clearStoreError, setWhatchedStoreInfo } = storesActions;
export default storesReducer;