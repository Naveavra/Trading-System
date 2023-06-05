import { Composore, DiscountType, Numeric, PredicateDataObject, XorDecidingRules } from "./Discount";
import { Node } from 'reactflow';
export interface RegularDiscountNode {
    label: string;
    parentNode?: string;
    percentage: Number;
    discountType: DiscountType;
    prodId: number;
    discountedCategory: string;
    predicates: PredicateDataObject[];
}
export interface CompositeDiscountNode {
    label: string;
    parentNode?: string;
    percentage: Number;
    numericType: Numeric;
    logicalType: Composore;
    xorDecidingRule: XorDecidingRules;
}
export type DiscountNodes = RegularDiscountNode | CompositeDiscountNode;
export type CustomNode = Node<DiscountNodes>;
