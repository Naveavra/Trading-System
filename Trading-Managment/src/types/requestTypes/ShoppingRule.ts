import { ShoppingRule } from "../systemTypes/ShoppingRule";

export interface GetStoreShoppingRulesParams {
    userId: number,
    storeId: number
};
export interface PostShoppingRuleParams {
    userId: number;
    storeId: number;
    purchasePolicy: ShoppingRule;
};
export interface deleteShoppingRuleParams {
    userId: number;
    storeId: number;
    purchasePolicyId: number;
};