import { ApiError, ApiListData } from "../types/apiTypes";
import { Product } from "../types/systemTypes/Product";

const reducerName = 'products';

interface ProductsState {
    productState: {
        isLoading: boolean;
        responseData?: Product;
        error?: ApiError;
        whatchedProduct?: Product;
    },
    isLoading: boolean;
    responseData?: ApiListData<Product>;
    error?: ApiError;
};

const initialState: ProductsState = {
    productState: {
        isLoading: false,
        responseData: undefined,
        error: undefined,
        whatchedProduct: undefined,
    },
    isLoading: false,
    responseData: undefined,
    error: undefined,
};
