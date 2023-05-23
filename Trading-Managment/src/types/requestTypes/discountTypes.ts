import { CompositeDataObject, Composore, DiscountDataObject, DiscountType, Numeric, PredicateDataObject, XorDecidingRules } from "../systemTypes/Discount";

export interface postRegularDicountParams {
    storeId: number;
    userId: number;
    percentage: Number;
    discountType: DiscountType;
    prodId: number;
    discountedCategory: string;
    predicates: PredicateDataObject[];
}
export interface postCompositeDicountParams {
    storeId: number;
    userId: number;
    precentage: Number;
    numericType: Numeric;
    locigalType: Composore;
    xorDecidingRule: XorDecidingRules;
    discounts: DiscountDataObject[];
    composores: CompositeDataObject[];
}
export interface deleteDiscountParams {
    storeId: number;
    userId: number;
    discountId: number;
}