import { Product } from "./Product";

export interface Order {
    orderId: number;
    userId: number;
    products: Product[];
    totalPrice: number;
}