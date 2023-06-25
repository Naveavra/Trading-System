import { Product } from "./Product";

export interface Order {
    orderId: number;
    userId: number;
    products: Product[];
    totalPrice: number;
}
export const emptyOrder: Order = {
    orderId: 0,
    userId: 0,
    products: [],
    totalPrice: 0,
}