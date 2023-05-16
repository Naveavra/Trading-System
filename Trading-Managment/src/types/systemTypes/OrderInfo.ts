import { Cart, emptyCart } from "./Cart";

export interface OrderInfo {
    orderId: number,
    userId: number,
    productsInStores: Cart
    totalPrice: number
}

export const EmptyOrder : OrderInfo = {
    orderId: -1, 
    userId: -1, 
    productsInStores: emptyCart, 
    totalPrice: 0,
}