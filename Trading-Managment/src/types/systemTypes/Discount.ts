export interface DiscountDataObject {
    percentage: Number;
    discountType: DiscountType;
    prodId: number;
    discountedCategory: string;
    predicates: PredicateDataObject[];
}


export interface PredicateDataObject {
    predType: PredicateType;
    params: string;
    composore: Composore;
}
export interface CompositeDataObject {
    precentage: Number;
    numericType: Numeric;
    locigalType: Composore;
    xorDecidingRule: XorDecidingRules;
    discounts: DiscountDataObject[];
    composores: CompositeDataObject[] | null;
}
export enum Numeric {
    Max = "Max",
    Min = "Min",
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