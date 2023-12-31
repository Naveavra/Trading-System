import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ShopRuleType, ShoppingRule, emptyShoppingRule, emptyShoppingRuleType } from "../types/systemTypes/ShoppingRule";
import { PostShoppingRuleParams, deleteShoppingRuleParams } from "../types/requestTypes/ShoppingRule";
import { ApiError } from "../types/apiTypes";
import { shoppingRuleApi } from "../api/shopRuleApi";

interface ShoppingRulesState {
    isLoading: boolean;
    error: string | null;
    currentShoppingRule: ShoppingRule;
    tmpShopRule: ShopRuleType;
    message: string;
}
const reducerName = 'ShoppingRulesState';
const initialState: ShoppingRulesState = {
    currentShoppingRule: emptyShoppingRule,
    tmpShopRule: emptyShoppingRuleType,
    isLoading: false,
    error: '',
    message: '',
}
export const addPurchasePolicy = createAsyncThunk
    <
        string,
        PostShoppingRuleParams,
        { rejectValue: ApiError }
    >
    (
        `${reducerName}/post`,
        async (formData, thunkApi) => {
            return shoppingRuleApi.postShoppingRule(formData)
                .then((res) => thunkApi.fulfillWithValue(res as string))
                .catch((res) => thunkApi.rejectWithValue(res as ApiError))
        });
export const deleteShoppingRule = createAsyncThunk<
    string,
    deleteShoppingRuleParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/delete`,
    async (formData, thunkApi) => {
        return shoppingRuleApi.deleteShoppingRule(formData)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });



const { reducer: shoppingRuleReducer, actions: shoppingRuleActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearShopRuleMessage: (state) => {
            state.message = '';
        },
        clearShopRuleError: (state) => {
            state.error = '';
        },
        clearTmpShopRule: (state) => {
            state.tmpShopRule = emptyShoppingRuleType;
        },
        clearCurrentShoppingRules: (state) => {
            state.currentShoppingRule = emptyShoppingRule;
        },
        addTmpToCurrent: (state) => {
            return {
                ...state,
                currentShoppingRule: {
                    ...state.currentShoppingRule,
                    type: [...state.currentShoppingRule.type, state.tmpShopRule]
                }
            }
        },
        setTmpShopRule: (state, { payload }) => {
            state.tmpShopRule = payload;
        },
        setDescritionToCurrent(state, { payload }) {
            return {
                ...state,
                currentShoppingRule: {
                    ...state.currentShoppingRule,
                    description: payload
                }
            }

        },
    },
    extraReducers: (builder) => {
        //addPurchasePolicy
        builder.addCase(addPurchasePolicy.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(addPurchasePolicy.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        })
        builder.addCase(addPurchasePolicy.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = state.error ? (state.error + payload?.message.data ?? 'Error during adding purchase policy') : (payload?.message.data ?? 'Error during adding purchase policy');
        });
        //deleteShoppingRule
        builder.addCase(deleteShoppingRule.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(deleteShoppingRule.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.message = payload;
        });
        builder.addCase(deleteShoppingRule.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = state.error ? (state.error + payload?.message.data ?? 'Error during deleting purchase policy') : (payload?.message.data ?? 'Error during deleting purchase policy');
        });
    }
});
export const { setDescritionToCurrent, clearCurrentShoppingRules, clearShopRuleError, clearShopRuleMessage, clearTmpShopRule, addTmpToCurrent, setTmpShopRule } = shoppingRuleActions;

export default shoppingRuleReducer;