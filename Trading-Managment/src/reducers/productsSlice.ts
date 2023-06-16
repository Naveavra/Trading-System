
import { Action, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse, ApiResponseListData } from "../types/apiTypes";
import { EmptyProduct, Product } from "../types/systemTypes/Product";
import { DeleteProductsParams, GetStoreProductsParams, PatchProductsParams, PostProductsParams } from "../types/requestTypes/productTypes";
import { productsApi } from "../api/productsApi";
import { store } from "../redux/store";
import { removeEmptyValues } from "../api/util";

const reducerName = 'products';

interface ProductsState {
    productState: {
        isLoading: boolean;
        responseData?: string | null;
        error: string | null;
        watchedProduct?: Product;
    },
    isLoading: boolean;
    responseData?: ApiListData<Product>;
    error: string | null;
};

const initialState: ProductsState = {
    productState: {
        isLoading: false,
        responseData: null,
        error: null,
        watchedProduct: undefined,
    },
    isLoading: false,
    responseData: [],
    error: null,
};

export const postProduct = createAsyncThunk<
    string,
    PostProductsParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/post`,
    async (formData, thunkApi) => {
        return productsApi.postProduct(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const patchProduct = createAsyncThunk<
    string,
    PatchProductsParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/patch`,
    async (formData, thunkApi) => {
        debugger;
        const data = removeEmptyValues(formData);
        return productsApi.patchProduct(data)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const deleteProduct = createAsyncThunk<
    string,
    DeleteProductsParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/delete`,
    async (formData, thunkApi) => {
        return productsApi.deleteProduct(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const getProducts = createAsyncThunk<
    ApiListData<Product>,
    void,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (_, thunkApi) => {
        return productsApi.getProducts()
            .then((res) => thunkApi.fulfillWithValue(res as ApiListData<Product>))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

const { reducer: productsReducer, actions: productsActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearProductsError: (state) => {
            state.error = null;
        },
        clearProductError: (state) => {
            state.error = null;
        },
        setWatchedProductInfo: (state, action) => {
            state.productState.watchedProduct = state.responseData?.find((product) => product.productId === action.payload) ?? EmptyProduct;
        },
        clearProductMsg: (state) => {
            state.productState.responseData = null;
        }
    },
    extraReducers: (builder) => {
        //getproducts
        builder.addCase(getProducts.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(getProducts.fulfilled, (state, { payload }) => { //payload is what we get back from the function 
            state.isLoading = false;
            state.responseData = payload;
            state.error = null;
        });
        builder.addCase(getProducts.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during getProducts";
        });
        //patchProduct
        builder.addCase(patchProduct.pending, (state) => {
            state.productState.isLoading = true;
            state.productState.error = null;
        });
        builder.addCase(patchProduct.fulfilled, (state, { payload }) => {
            state.productState.isLoading = false;
            state.productState.responseData = payload;
        });
        builder.addCase(patchProduct.rejected, (state, { payload }) => {
            state.productState.error = payload?.message.data ?? "error during patchProducts";
            state.productState.isLoading = false;
        });
        //postProduct
        builder.addCase(postProduct.pending, (state) => {
            state.productState.isLoading = true;
            state.productState.error = null;
        });
        builder.addCase(postProduct.fulfilled, (state, { payload }) => {
            state.productState.isLoading = false;
            state.productState.responseData = payload;
        });
        builder.addCase(postProduct.rejected, (state, { payload }) => {
            state.productState.isLoading = false;
            state.productState.error = payload?.message.data ?? "error during postProduct";
        });
        //deleteProduct
        builder.addCase(deleteProduct.pending, (state) => {
            state.productState.isLoading = true;
            state.productState.error = null;
        });
        builder.addCase(deleteProduct.fulfilled, (state, { payload }) => {
            state.productState.isLoading = false;
            state.productState.responseData = payload;
        });
        builder.addCase(deleteProduct.rejected, (state, { payload }) => {
            state.productState.error = payload?.message.data ?? "error during deleteProduct";
            state.productState.isLoading = false;
        });



    }
});

export const { clearProductMsg, clearProductError, clearProductsError, setWatchedProductInfo } = productsActions;
export default productsReducer;



