import { Action, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse } from "../types/apiTypes";

import { storeApi } from "../api/storeApi";
import { AnswerQuestionParams, AppointUserParams, DeleteStoreParams, GetStoresParams, PatchStoreParams, PostStoreParams, WriteQuestionParams, WriteReviewOnProductParams, WriteReviewParams, fireUserParams, patchPermissionsParams } from "../types/requestTypes/storeTypes";
import { Store, emptyStore } from "../types/systemTypes/Store";
import { StoreInfo, emptyStoreInfo } from "../types/systemTypes/StoreInfo";
import { store } from "../redux/store";
import { removeEmptyValues } from "../api/util";

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
    storeInfoResponseData: StoreInfo[];
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
    storeInfoResponseData: [],
    error: null,
};

export const postStore = createAsyncThunk<
    number,
    PostStoreParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/post`,
    async (formData, thunkApi) => {
        return storeApi.postStore(formData)
            .then((res) => thunkApi.fulfillWithValue(res as number))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const patchStore = createAsyncThunk<
    string,
    PatchStoreParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/patch`,
    async (formData, thunkApi) => {
        debugger;
        const data = removeEmptyValues(formData);
        return storeApi.patchStore(data)
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
    StoreInfo[],
    void,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (_, thunkApi) => {
        return storeApi.getStoresInfo()
            .then((res) => thunkApi.fulfillWithValue(res as StoreInfo[]))
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
    AppointUserParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/appointments/managers/post`,
    async (params, thunkApi) => {
        return storeApi.appointManager(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const appointOwner = createAsyncThunk<
    string,
    AppointUserParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/appointments/owners/post`,
    async (params, thunkApi) => {
        return storeApi.appointOwner(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const fireManager = createAsyncThunk<
    string,
    fireUserParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/appointments/managers/delete`,
    async (params, thunkApi) => {
        return storeApi.fireManager(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const fireOwner = createAsyncThunk<
    string,
    fireUserParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/appointments/owners/delete`,
    async (params, thunkApi) => {
        return storeApi.fireOwner(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const patchPermissions = createAsyncThunk<
    string,
    patchPermissionsParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/permissions/patch`,
    async (params, thunkApi) => {
        return storeApi.patchPermissions(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });


export const answerQuestion = createAsyncThunk<
    string,
    AnswerQuestionParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/questions/answer`,
    async (params, thunkApi) => {
        return storeApi.amswerQuestion(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
export const writeReview = createAsyncThunk<
    string,
    WriteReviewParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/reviews/write`,
    async (params, thunkApi) => {
        return storeApi.writeReview(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
export const writeReviewOnProduct = createAsyncThunk<
    string,
    WriteReviewOnProductParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/reviews/writeOnProduct`,
    async (params, thunkApi) => {
        return storeApi.writeReviewOnProduct(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
export const writeQuestion = createAsyncThunk<
    string,
    WriteQuestionParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/questions/write`,
    async (params, thunkApi) => {
        return storeApi.writeQuestion(params)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });


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
            debugger;
            state.storeState.wahtchedStoreInfo = state.storeInfoResponseData.find((storeInfo) => storeInfo.storeId === action.payload) ?? emptyStoreInfo;
        },
        clearStoresResponse: (state, action) => {
            state.storeState.responseData = null;
        }
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
            state.storeState.responseData = "store created successfully";
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
        //appointOwner
        builder.addCase(appointOwner.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(appointOwner.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(appointOwner.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        //fireOwner
        builder.addCase(fireOwner.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(fireOwner.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(fireOwner.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        //fireManager
        builder.addCase(fireManager.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(fireManager.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(fireManager.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        //patchPermmision
        builder.addCase(patchPermissions.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(patchPermissions.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(patchPermissions.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        //answerQuestion
        builder.addCase(answerQuestion.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(answerQuestion.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(answerQuestion.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        builder.addCase(writeReview.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(writeReview.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(writeReview.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        builder.addCase(writeQuestion.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(writeQuestion.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(writeQuestion.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });
        builder.addCase(writeReviewOnProduct.pending, (state) => {
            state.storeState.isLoading = true;
            state.storeState.error = null;
        });
        builder.addCase(writeReviewOnProduct.rejected, (state, { payload }) => {
            state.storeState.error = payload?.message.data ?? "error during getStore";
            state.storeState.isLoading = false;
        });
        builder.addCase(writeReviewOnProduct.fulfilled, (state, { payload }) => {
            state.storeState.isLoading = false;
            state.storeState.responseData = payload;
        });

    }
});


// Action creators are generated for each case reducer function
export const { clearStoresError, clearStoreError, setWhatchedStoreInfo, clearStoresResponse } = storesActions;
export default storesReducer;