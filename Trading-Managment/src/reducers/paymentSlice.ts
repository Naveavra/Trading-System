import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { paymentApi } from "../api/paymentApi";
import { ApiError } from "../types/apiTypes";
import { CancelPayParams, HandShakeParams, PayParams } from "../types/requestTypes/paymentTypes";

interface PaymentState {
    isLoading: boolean;
    responseData: string | number | null;
    error: string | null;
};
const reducerName = 'payment';
const initialState: PaymentState = {
    isLoading: false,
    responseData: null,
    error: null,
};

const handshake = createAsyncThunk<
    string,
    HandShakeParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/handshake`,
    async (formData, thunkApi) => {
        return paymentApi.handshake()
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    }
);

const pay = createAsyncThunk<
    number,
    PayParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/pay`,
    async (formData, thunkApi) => {
        return paymentApi.pay(formData)
            .then((res) => thunkApi.fulfillWithValue(res as number))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    }
);

const cancelPay = createAsyncThunk<
    number,
    CancelPayParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/cancelPay`,
    async (formData, thunkApi) => {
        return paymentApi.cancelPay(formData)
            .then((res) => thunkApi.fulfillWithValue(res as number))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    }
);

const { reducer: paymentReducer, actions: paymentActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        reset: () => initialState,
    },
    extraReducers: (builder) => {
        //handshake
        builder.addCase(handshake.pending, (state) => {
            state.isLoading = true;
            state.responseData = null;
            state.error = null;
        });
        builder.addCase(handshake.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = payload;
            state.error = null;
        });
        builder.addCase(handshake.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = null;
            state.error = payload?.message?.data ?? "error during handshake";
        });
        //pay
        builder.addCase(pay.pending, (state) => {
            state.isLoading = true;
            state.responseData = null;
            state.error = null;
        });
        builder.addCase(pay.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = payload;
            state.error = null;
        });
        builder.addCase(pay.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = null;
            state.error = payload?.message?.data ?? "error during pay";
        });
        //cancelPay
        builder.addCase(cancelPay.pending, (state) => {
            state.isLoading = true;
            state.responseData = null;
            state.error = null;
        });
        builder.addCase(cancelPay.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = payload;
            state.error = null;
        });
        builder.addCase(cancelPay.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = null;
            state.error = payload?.message?.data ?? "error during cancelPay";
        });

    },
});
export const { reset } = paymentActions;
export default paymentReducer;
