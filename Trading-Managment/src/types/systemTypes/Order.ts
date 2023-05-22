import { Product } from "./Product";

export interface Order {
    orderId: number;
    userId: number;
    productsInStores: Product[];
    totalPrice: number;
}