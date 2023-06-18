import { Product } from "./Product";

export interface Order {
    orderId: number;
    userId: number;
    productsInStores: Product[];
    totalPrice: number;
}
export const emptyOrder: Order = {
    orderId: 0,
    userId: 0,
    productsInStores: [],
    totalPrice: 0,
}