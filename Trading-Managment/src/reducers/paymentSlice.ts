import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { Supplier, paymentService } from "../types/systemTypes/Supplier";
import { paymentApi } from "../api/paymentApi";
import { ApiError } from "../types/apiTypes";

interface PaymentState {
    isLoading: boolean;
    responseData: string | number | null;
    suppliers: Supplier[];
    paymentServices: paymentService[];
    error: string | null;
};
const reducerName = 'payment';
const initialState: PaymentState = {
    isLoading: false,
    responseData: null,
    suppliers: [],
    paymentServices: [],
    error: null,
};

export const getSuppliers = createAsyncThunk<
    Supplier[],
    void,
    { rejectValue: ApiError }
>(
    `${reducerName}/getSuppliers`,
    async (_, thunkApi) => {
        return paymentApi.getSuppliers()
            .then((res) => thunkApi.fulfillWithValue(res as Supplier[]))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const getPaymentsService = createAsyncThunk<
    paymentService[],
    void,
    { rejectValue: ApiError }
>(
    `${reducerName}/getPaymentsService`,
    async (_, thunkApi) => {
        return paymentApi.getPaymentsService()
            .then((res) => thunkApi.fulfillWithValue(res as paymentService[]))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });


const { reducer: paymentReducer, actions: paymentActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearPaymentError: (state) => {
            state.error = null;
        },
        clearPaymnetResponseData: (state) => {
            state.responseData = null;
        }
    },
    extraReducers: (builder) => {
        //
        builder.addCase(getSuppliers.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(getSuppliers.fulfilled, (state, { payload }) => { //payload is what we get back from the function
            state.isLoading = false;
            state.suppliers = payload;
            state.error = null;
        });
        builder.addCase(getSuppliers.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during getSuppliers";
        });
        //
        builder.addCase(getPaymentsService.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(getPaymentsService.fulfilled, (state, { payload }) => { //payload is what we get back from the function
            state.isLoading = false;
            state.paymentServices = payload;
            state.error = null;
        });
        builder.addCase(getPaymentsService.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during getPaymentsService";
        });
    },
});
export const { clearPaymentError, clearPaymnetResponseData } = paymentActions;
export default paymentReducer;
