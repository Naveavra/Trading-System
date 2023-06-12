import { Edge, Node } from "reactflow";
import { DiscountType, PredicateDataObject } from "../systemTypes/Discount";
import { DiscountNodes } from "../systemTypes/DiscountNodes";

export interface postRegularDicountParams {
    storeId: number;
    userId: number;
    description: string;
    percentage: Number;
    discountType: DiscountType;
    prodId: number;
    discountedCategory: string;
    predicates: PredicateDataObject[];
}
export interface postCompositeDicountParams {
    storeId: number;
    userId: number;
    description: string;
    discountNodes: Node<DiscountNodes>[];
    discountEdges: Edge[];
}
export interface deleteDiscountParams {
    storeId: number;
    userId: number;
    discountId: number;
}