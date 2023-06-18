import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { bidApi } from "../api/bidApi";
import { ApiError } from "../types/apiTypes";
import { addBidParams, answerBidParams, buyProductInBidParams, clientAnswerParams, counterBidParams, editBidParams } from "../types/requestTypes/bidTypes";

interface BidState {
    isLoading: boolean;
    error: string | null;
    message: string | null;
}
const reducerName = 'bidSlice';
const initialState: BidState = {
    isLoading: false,
    error: '',
    message: '',
}

export const addBid = createAsyncThunk<
    string,
    addBidParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/addBid`,
    async (params, thunkAPI) => {
        return bidApi.postBid(params)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });

export const answerBid = createAsyncThunk<
    string,
    answerBidParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/answerBid`,
    async (params, thunkAPI) => {
        return bidApi.postAnswerBid(params)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });
export const counterBid = createAsyncThunk<
    string,
    counterBidParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/counterBid`,
    async (params, thunkAPI) => {
        return bidApi.postCounterBid(params)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });
export const editBid = createAsyncThunk<
    string,
    editBidParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/editBid`,
    async (params, thunkAPI) => {
        return bidApi.editBid(params)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });
export const buyProductInBid = createAsyncThunk<
    string,
    buyProductInBidParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/buyProductInBid`,
    async (params, thunkAPI) => {
        return bidApi.buyProductInBid(params)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });
    export const answerOnCounter = createAsyncThunk<
    string,
    clientAnswerParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/answerOnCounter`,
    async (params, thunkAPI) => {
        return bidApi.clientAnswer(params)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });

const { reducer: bidReducer, actions: bidActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearBidMsg: (state) => {
            state.message = null;
        },
        clearBidError: (state) => {
            state.error = null;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(addBid.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(addBid.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        });
        builder.addCase(addBid.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during add bid";
        });
        builder.addCase(answerBid.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(answerBid.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        });
        builder.addCase(answerBid.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during answer bid";
        });
        builder.addCase(counterBid.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(counterBid.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        });
        builder.addCase(counterBid.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during counter bid";
        });
        builder.addCase(editBid.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(editBid.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        });
        builder.addCase(editBid.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during edit bid";
        });
        builder.addCase(buyProductInBid.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(buyProductInBid.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        });
        builder.addCase(buyProductInBid.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during edit bid";
        });
        builder.addCase(answerOnCounter.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(answerOnCounter.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        });
        builder.addCase(answerOnCounter.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during edit bid";
        });
    }
});
export const { clearBidMsg, clearBidError } = bidActions;
export default bidReducer;