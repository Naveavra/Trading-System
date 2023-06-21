import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError } from "../types/apiTypes";
import { DeleteCartParams, GetCartParams, PatchCartParams, buyCartParams } from "../types/requestTypes/cartTypes";
import { cartApi } from "../api/cartApi";
import { Product } from "../types/systemTypes/Product";

const reducerName = 'carts';

interface CartState {
    basketState: {
        isLoading: boolean;
        responseData?: string | null;
        error: string | null;
    },
    isLoading: boolean;
    responseData: Product[];
    error: string | null;
};

const initialState: CartState = {
    basketState: {
        isLoading: false,
        responseData: null,
        error: null,
    },
    isLoading: false,
    responseData: [],
    error: null,
};
export const patchCart = createAsyncThunk<
    string,
    PatchCartParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/patch`,
    async (formData, thunkApi) => {
        return cartApi.patchCart(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const deleteCart = createAsyncThunk<
    string,
    DeleteCartParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/delete`,
    async (formData, thunkApi) => {
        return cartApi.deleteCart(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const getCart = createAsyncThunk<
    Product[],
    GetCartParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (formData, thunkApi) => {
        return cartApi.getCart(formData)
            .then((res) => thunkApi.fulfillWithValue(res as Product[]))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

//add to cart 
export const addToCart = createAsyncThunk<
    string,
    PatchCartParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/addToCart`,
    async (formData, thunkApi) => {
        return cartApi.addToCart(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

//remove from cart
export const removeFromCart = createAsyncThunk<
    string,
    PatchCartParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/removeFromCart`,
    async (formData, thunkApi) => {
        return cartApi.removeFromCart(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
//buy
export const buyCart = createAsyncThunk<
    string,
    buyCartParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/buyCart`,
    async (formData, thunkApi) => {
        return cartApi.buyCart(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

const { reducer: cartReducer, actions: cartActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearCartError: (state) => {
            state.basketState.error = null;
        },
        clearCartMessage: (state) => {
            state.basketState.responseData = null;
        },
        clearCartsError: (state) => {
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        //getcart
        builder.addCase(getCart.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(getCart.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = payload;
        });
        builder.addCase(getCart.rejected, (state, { payload }) => {
            state.isLoading = false;
            if (payload?.message.data != 'the id given does not belong to any member') {
                state.error = state.error ? (state.error + payload?.message.data ?? "error during getCart") : payload?.message.data ?? "error during getCart";
            }
        });
        //patchcart
        builder.addCase(patchCart.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(patchCart.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.basketState.responseData = payload;
        });
        builder.addCase(patchCart.rejected, (state, { payload }) => {
            state.error = state.error ? (state.error + payload?.message.data ?? "error during getCart") : payload?.message.data ?? "error during patchCart";
            state.isLoading = false;
        });
        //addToCart
        builder.addCase(addToCart.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(addToCart.fulfilled, (state, { payload }) => {
            state.basketState.responseData = payload;
        });
        builder.addCase(addToCart.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = state.error ? (state.error + payload?.message.data ?? "error during add to Cart") : payload?.message.data ?? "error during addToCart";
        });
        //deleteProduct
        builder.addCase(deleteCart.pending, (state) => {
            state.basketState.isLoading = true;
        });
        builder.addCase(deleteCart.fulfilled, (state, { payload }) => {
            state.basketState.isLoading = false;
            state.basketState.responseData = payload;
        });
        builder.addCase(deleteCart.rejected, (state, { payload }) => {
            state.basketState.error = state.basketState.error ? (state.basketState.error + payload?.message.data ?? "error during deleteCart") : payload?.message.data ?? "error during deleteCart";
            state.basketState.isLoading = false;
        });
        //buy cart
        builder.addCase(buyCart.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(buyCart.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.basketState.responseData = payload;
        });
        builder.addCase(buyCart.rejected, (state, { payload }) => {
            state.error = state.error ? (state.error + payload?.message.data ?? "error during buyCart") : payload?.message.data ?? "error during buyCart";
            state.isLoading = false;
        });
        //remove from cart
        builder.addCase(removeFromCart.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(removeFromCart.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.basketState.responseData = payload;
        });
        builder.addCase(removeFromCart.rejected, (state, { payload }) => {
            state.error = state.error ? (state.error + payload?.message.data ?? "error during remove From Cart") : payload?.message.data ?? "error during removeFromCart";
            state.isLoading = false;
        });
    }
});

export const { clearCartMessage, clearCartsError, clearCartError } = cartActions;
export default cartReducer;




