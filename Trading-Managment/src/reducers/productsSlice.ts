import { Action, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError, ApiListData, ApiResponse, ApiResponseListData} from "../types/apiTypes";
import { EmptyProduct, Product } from "../types/systemTypes/Product";
import { ProductResponseData } from "../types/responseTypes/productTypes";
import { DeleteProductsParams, GetProductsParams, PatchProductsParams, PostProductsParams } from "../types/requestTypes/GetProductsParams";
import { productsApi } from "../api/productsApi";

const reducerName = 'products';

interface ProductsState {
    productState: {
        isLoading: boolean;
        responseData?: Product | null;
        error: string | null;
        watchedProduct?: Product;
    },
    isLoading: boolean;
    responseData?: ApiListData<Product>;
    error:  string | null;
};

const initialState: ProductsState = {
    productState: {
        isLoading: false,
        responseData: null,
        error: null,
        watchedProduct: undefined,
    },
    isLoading: false,
    responseData: { data: {results: []}},
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
        return productsApi.patchProduct(formData)
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
    GetProductsParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (formData, thunkApi) => {
        return productsApi.getProducts(formData)
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
            state.productState.watchedProduct = state.responseData?.data.results.find((product) => product.id === action.payload) ?? EmptyProduct;
        },
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
            state.error = payload?.message.data ?? "error during getStores";
        });
        //patchStore
        // builder.addCase(patchProduct.pending, (state) => {
        //     state.storeState.isLoading = true;
        //     state.storeState.error = null;
        // });
        // builder.addCase(patchProduct.fulfilled, (state, { payload }) => {
        //     state.storeState.isLoading = false;
        //     state.storeState.responseData = payload;
        // });
        // builder.addCase(patchProduct.rejected, (state, { payload }) => {
        //     state.storeState.error = payload?.message.data ?? "error during patchStore";
        //     state.storeState.isLoading = false;
        // });


    }
});

export const { clearProductError, clearProductsError, setWatchedProductInfo } = productsActions;
export default productsReducer;



