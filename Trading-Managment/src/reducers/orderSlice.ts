import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError, ApiListData } from "../types/apiTypes";
import { OrderInfo } from "../types/systemTypes/OrderInfo";
import { getOrderParams, postOrderParams } from "../types/requestTypes/orderTypes";
import { orderApi } from "../api/orderApi";
import { EmptyOrder } from "../types/systemTypes/OrderInfo";
const reducerName = 'orders';

interface OrderState {
    orderState: {
        isLoading: boolean;
        responseData?: string | null;
        error: string | null;
        watchedOrder?: OrderInfo;
    },
    isLoading: boolean;
    responseData?: ApiListData<OrderInfo>;
    error: string | null;
};

const initialState: OrderState = {
    orderState: {
        isLoading: false,
        responseData: null,
        error: null,
        watchedOrder: undefined,
    },
    isLoading: false,
    responseData: [],
    error: null,
}

export const postOrder = createAsyncThunk<
    string,
    postOrderParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/post`,
    async (formData, thunkApi) => {
        return orderApi.postOrder(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const getOrders = createAsyncThunk<
    ApiListData<OrderInfo>,
    getOrderParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (formData, thunkApi) => {
        return orderApi.getOrder(formData)
            .then((res) => thunkApi.fulfillWithValue(res as ApiListData<OrderInfo>))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

const { reducer: orderReducer, actions: orderActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearOrdersError: (state) => {
            state.error = null;
        },
        clearOrderError: (state) => {
            state.error = null;
        },
        setWatchedOrderInfo: (state, action) => {
            state.orderState.watchedOrder = state.responseData?.find((order) => order.orderId === action.payload) ?? EmptyOrder;
        },
    },
    extraReducers: (builder) => {
        //getproducts
        builder.addCase(getOrders.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(getOrders.fulfilled, (state, { payload }) => { //payload is what we get back from the function 
            state.isLoading = false;
            state.responseData = payload;
            state.error = null;
        });
        builder.addCase(getOrders.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during getProducts";
        });
        //PostProduct
        builder.addCase(postOrder.pending, (state) => {
            state.orderState.isLoading = true;
            state.orderState.error = null;
        });
        builder.addCase(postOrder.fulfilled, (state, { payload }) => {
            state.orderState.isLoading = false;
            state.orderState.responseData = payload;
        });
        builder.addCase(postOrder.rejected, (state, { payload }) => {
            state.orderState.error = payload?.message.data ?? "error during patchProducts";
            state.orderState.isLoading = false;
        });
    }
});

export const { clearOrderError, clearOrdersError, setWatchedOrderInfo } = orderActions;
export default orderReducer;