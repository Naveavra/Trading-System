import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse} from "../types/apiTypes";
import { Basket } from "../types/systemTypes/Basket";
import { DeleteCartParams, GetCartParams, PatchCartParams, PostBasketParams } from "../types/requestTypes/cartTypes";
import { cartApi } from "../api/cartApi";
import { Cart } from "../types/systemTypes/Cart";

const reducerName = 'carts';

interface CartState {
    basketState: {
        isLoading: boolean;
        responseData?: string  | null;
        error: string | null;
    },
    isLoading: boolean;
    responseData?: ApiResponse<Cart> | null ;
    error:  string | null;
};

const initialState: CartState = {
    basketState: {
        isLoading: false,
        responseData: null,
        error: null,
    },
    isLoading: false,
    responseData: null,
    error: null,
};
export const postBasket = createAsyncThunk<
    string,
    PostBasketParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/post`,
    async (formData, thunkApi) => {
        return cartApi.postBaket(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const patchCart = createAsyncThunk<
    string,
    PatchCartParams ,
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
    ApiResponse<Cart>,
    GetCartParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (formData, thunkApi) => {
        return cartApi.getCart(formData)
            .then((res) => thunkApi.fulfillWithValue(res as ApiResponse<Cart>))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

const { reducer: cartReducer, actions: cartActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearBasketError: (state) => {
            state.error = null;
        },
        clearCartError: (state) => {
            state.error = null;
        },
    },
    extraReducers: (builder) => {
        //getcart
        builder.addCase(getCart.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(getCart.fulfilled, (state, { payload }) => { //payload is what we get back from the function 
            state.isLoading = false;
            state.responseData = payload;
            console.log("response: " ,state.responseData);
            state.error = null;
        });
        builder.addCase(getCart.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during getCart";
        });
        //patchcart
        builder.addCase(patchCart.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(patchCart.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.basketState.responseData = payload;
            console.log(payload);
            state.error = null;
        });
        builder.addCase(patchCart.rejected, (state, { payload }) => {
            state.error = payload?.message.data ?? "error during patchCart";
            state.isLoading = false;
        });
        //postBaket
        builder.addCase(postBasket.pending, (state) => {
            state.basketState.isLoading = true;
            state.basketState.error = null;
        });
        builder.addCase(postBasket.fulfilled, (state, { payload }) => {
            state.basketState.isLoading = false;
            state.basketState.responseData = payload;
        });
        builder.addCase(postBasket.rejected, (state, { payload }) => {
            state.basketState.isLoading = false;
            state.basketState.error = payload?.message.data ?? "error during postBasket";
        });
        //deleteProduct
        builder.addCase(deleteCart.pending, (state) => {
            state.basketState.isLoading = true;
            state.basketState.error = null;
        });
        builder.addCase(deleteCart.fulfilled, (state, { payload }) => {
            state.basketState.isLoading = false;
            state.basketState.responseData = payload;
        });
        builder.addCase(deleteCart.rejected, (state, { payload }) => {
            state.basketState.error = payload?.message.data ?? "error during deleteCart";
            state.basketState.isLoading = false;
        });
        


    }
});

export const { clearBasketError, clearCartError} = cartActions;
export default cartReducer;




