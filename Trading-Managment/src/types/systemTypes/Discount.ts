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
    discounts: DiscountDataObject[];
    composores: CompositeDataObject[];
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
}
export enum DiscountType {
    Product = "Product",
    Category = "Category",
    Store = "Store",
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
export const empryRegularDiscount: DiscountDataObject = {
    percentage: 0,
    discountType: DiscountType.Product,
    prodId: 0,
    discountedCategory: "",
    predicates: []
}
export const empryCompositeDiscount: CompositeDataObject = {
    percentage: 0,
    numericType: Numeric.Max,
    logicalType: Composore.AND,
    xorDecidingRule: XorDecidingRules.MaxDiscountValue,
    discounts: [],
    composores: []
}
export const emptyPredicate: PredicateDataObject = {
    predType: PredicateType.MinPrice,
    params: "",
    composore: Composore.AND
}