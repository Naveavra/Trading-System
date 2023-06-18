import { Composore, DiscountType, Numeric, PredicateDataObject, XorDecidingRules } from "./Discount";
import { Node } from 'reactflow';
export interface RegularDiscountNode {
    type: 'regular';
    description: string;
    label: string;
    parentNode?: string;
    percentage: Number;
    discountType: DiscountType;
    prodId: number;
    discountedCategory: string;
    predicates: PredicateDataObject[];
}
export interface CompositeDiscountNode {
    type: 'composite';
    description: string;
    label: string;
    parentNode?: string;
    percentage: Number;
    numericType: Numeric;
    logicalType: Composore;
    xorDecidingRule: XorDecidingRules;
}
export type DiscountNodes = RegularDiscountNode | CompositeDiscountNode;
export type CustomNode = Node<DiscountNodes>;
