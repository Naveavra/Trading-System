import { RegularDiscountNode, CompositeDiscountNode } from "./DiscountNodes";

export type Discount = DiscountDataObject | CompositeDataObject;
export interface DiscountDataObject {
    percentage: Number;
    discountType: DiscountType;
    prodId: number;
    discountedCategory: string;
    predicates: PredicateDataObject[];
}

export interface CompositeDataObject {
    percentage: Number;
    numericType: Numeric;
    logicalType: Composore;
    xorDecidingRule: XorDecidingRules;
}
export interface PredicateDataObject {
    predType: PredicateType;
    params: string;
    composore: Composore;
}
export enum Numeric {
    Max = "Max",
    Addition = "Addition",
}
export enum Composore {
    AND = "And",
    OR = "Or",
    XOR = "Xor",
    NULL = "Null",
}
export enum DiscountType {
    Product = "Product",
    Category = "Category",
    Store = "Store",
    Regular = "Regular"
};
export enum PredicateType {
    MinPrice = "MinPrice",
    MaxPrice = "MaxPrice",
    MinNumOfItem = "MinNumOfItem",
    MaxNumOfItem = "MaxNumOfItem",
    MinNumFromCategory = "MinNumFromCategory",
    MaxNumFromCategory = "MaxNumFromCategory",
}
export enum XorDecidingRules {
    MaxDiscountValue = "MaxDiscountValue",
    MinDiscountValue = "MinDiscountValue"
}
export const emptyRegularDiscount: RegularDiscountNode = {
    type: 'regular',
    description: "",
    label: "",
    percentage: 0,
    discountType: DiscountType.Product,
    prodId: 0,
    discountedCategory: "",
    predicates: [],
}
export const emptyCompositeDiscount: CompositeDiscountNode = {
    type: 'composite',
    description: "",
    label: "",
    percentage: 0,
    numericType: Numeric.Max,
    logicalType: Composore.AND,
    xorDecidingRule: XorDecidingRules.MaxDiscountValue,

}
export const emptyPredicate: PredicateDataObject = {
    predType: PredicateType.MinPrice,
    params: "",
    composore: Composore.AND
}