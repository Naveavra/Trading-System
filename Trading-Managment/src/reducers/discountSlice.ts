import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { discountApi } from "../api/discountApi";
import { ApiError } from "../types/apiTypes";
import { CompositeDataObject, Discount, DiscountDataObject, empryCompositeDiscount, empryRegularDiscount } from "../types/systemTypes/Discount";
import { deleteDiscountParams, postCompositeDicountParams, postRegularDicountParams } from "../types/requestTypes/discountTypes";

const reducerName = 'discounts';

interface DiscountState {
    discountState: {
        isLoading: boolean;
        responseData?: string | null;
        error: string | null;
    },
    isLoading: boolean;
    responseData: Discount[];
    error: string | null;
    currentRegularDiscount: DiscountDataObject;
    currentCompositeDiscount: CompositeDataObject;
};

const initialState: DiscountState = {
    discountState: {
        isLoading: false,
        responseData: null,
        error: null,
    },
    isLoading: false,
    responseData: [],
    error: null,
    currentRegularDiscount: empryRegularDiscount,
    currentCompositeDiscount: empryCompositeDiscount,
};


export const getDiscounts = createAsyncThunk<
    Discount[],
    void,
    { rejectValue: ApiError }
>(
    `${reducerName}/get`,
    async (_, thunkApi) => {
        return discountApi.getDiscounts()
            .then((res) => thunkApi.fulfillWithValue(res as Discount[]))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const addRegularDiscount = createAsyncThunk<
    string,
    postRegularDicountParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/postRegularDiscount`,
    async (formData, thunkApi) => {
        return discountApi.postRegularDiscount(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const addCompositeDiscount = createAsyncThunk<
    string,
    postCompositeDicountParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/postCompositeDiscount`,
    async (formData, thunkApi) => {
        return discountApi.postCompositeDiscount(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const deleteDiscount = createAsyncThunk<
    string,
    deleteDiscountParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/delete`,
    async (formData, thunkApi) => {
        return discountApi.deleteDiscount(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });


const { reducer: discountReducer, actions: discountActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        setCurrentRegularDiscount: (state, { payload }) => {
            state.currentRegularDiscount = payload;
        },
        setpercentageToRegularDiscount: (state, { payload }) => {
            return {
                ...state,
                currentRegularDiscount: {
                    ...state.currentRegularDiscount,
                    percentage: payload
                }
            }
        },
        setDiscountTypeToRegularDiscount: (state, { payload }) => {
            return {
                ...state,
                currentRegularDiscount: {
                    ...state.currentRegularDiscount,
                    discountType: payload
                }
            }
        },
        setProductIdToRegularDiscount: (state, { payload }) => {
            return {
                ...state,
                currentRegularDiscount: {
                    ...state.currentRegularDiscount,
                    prodId: payload
                }
            }
        },
        setCategoryToRegularDiscount: (state, { payload }) => {
            return {
                ...state,
                currentRegularDiscount: {
                    ...state.currentRegularDiscount,
                    discountedCategory: payload
                }
            }
        },
        addPredicateToRegularDiscount: (state, { payload }) => {
            state.currentRegularDiscount?.predicates.push(payload);
        },
        //-------------------composite-------------------
        setCurrentCompositeDiscount: (state, { payload }) => {
            state.currentCompositeDiscount = payload;
        },
        setpercentageToCompositeDiscount: (state, { payload }) => {
            return {
                ...state,
                currentCompositeDiscount: {
                    ...state.currentCompositeDiscount,
                    percentage: payload
                }
            }
        },
        setNumericTypeToCompositeDiscount: (state, { payload }) => {
            return {
                ...state,
                currentCompositeDiscount: {
                    ...state.currentCompositeDiscount,
                    numericType: payload
                }
            }
        },
        setlogicalTypeToCompositeDiscount: (state, { payload }) => {
            return {
                ...state,
                currentCompositeDiscount: {
                    ...state.currentCompositeDiscount,
                    logicalType: payload
                }
            }
        },
        setXorDecidingRuleToCompositeDiscount: (state, { payload }) => {
            return {
                ...state,
                currentCompositeDiscount: {
                    ...state.currentCompositeDiscount,
                    xorDecidingRule: payload
                }
            }
        },
        addComposoreToCompositeDiscount: (state, { payload }) => {
            state.currentCompositeDiscount?.composores.push(payload);
        },
        addDiscountToCompositeDiscount: (state, { payload }) => {
            state.currentCompositeDiscount?.discounts.push(payload);
        },
        clearDiscountError: (state) => {
            state.discountState.error = null;
        },
        clearDiscountsError: (state) => {
            state.error = null;
        },
        cleanRegularDiscount: (state) => {
            state.currentRegularDiscount = empryRegularDiscount;
        },
        cleanCompositeDiscount: (state) => {
            state.currentCompositeDiscount = empryCompositeDiscount;
        },
    },
    extraReducers: (builder) => {
        //getDiscounts
        builder.addCase(getDiscounts.pending, (state) => {
            state.isLoading = true;
            state.error = null;
        });
        builder.addCase(getDiscounts.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.responseData = payload;
        });
        builder.addCase(getDiscounts.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during getDiscounts";
        });
        //addRegularDiscount
        builder.addCase(addRegularDiscount.pending, (state) => {
            state.discountState.isLoading = true;
            state.discountState.error = null;
        });
        builder.addCase(addRegularDiscount.fulfilled, (state, { payload }) => {
            state.discountState.isLoading = false;
            state.discountState.responseData = payload;
        });
        builder.addCase(addRegularDiscount.rejected, (state, { payload }) => {
            state.discountState.isLoading = false;
            state.discountState.error = payload?.message.data ?? "error during addRegularDiscount";
        });
        //addCompositeDiscount
        builder.addCase(addCompositeDiscount.pending, (state) => {
            state.discountState.isLoading = true;
            state.discountState.error = null;
        });
        builder.addCase(addCompositeDiscount.fulfilled, (state, { payload }) => {
            state.discountState.isLoading = false;
            state.discountState.responseData = payload;
        });
        builder.addCase(addCompositeDiscount.rejected, (state, { payload }) => {
            state.discountState.isLoading = false;
            state.discountState.error = payload?.message.data ?? "error during addCompositeDiscount";
        });
        //deleteDiscount
        builder.addCase(deleteDiscount.pending, (state) => {
            state.discountState.isLoading = true;
            state.discountState.error = null;
        });
        builder.addCase(deleteDiscount.fulfilled, (state, { payload }) => {
            state.discountState.isLoading = false;
            state.discountState.responseData = payload;
        });
        builder.addCase(deleteDiscount.rejected, (state, { payload }) => {
            state.discountState.isLoading = false;
            state.discountState.error = payload?.message.data ?? "error during deleteDiscount";
        });
    },
});

export const {
    setCurrentRegularDiscount,
    setpercentageToRegularDiscount,
    setDiscountTypeToRegularDiscount,
    setProductIdToRegularDiscount,
    setCategoryToRegularDiscount,
    addPredicateToRegularDiscount,
    setCurrentCompositeDiscount,
    setpercentageToCompositeDiscount,
    setNumericTypeToCompositeDiscount,
    setlogicalTypeToCompositeDiscount,
    setXorDecidingRuleToCompositeDiscount,
    addComposoreToCompositeDiscount,
    addDiscountToCompositeDiscount,
    clearDiscountError,
    clearDiscountsError,
    cleanRegularDiscount,
} = discountActions;
export default discountReducer;

