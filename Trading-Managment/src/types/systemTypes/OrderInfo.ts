import { Basket } from "./Basket"


export interface OrderInfo {
    orderId: number,
    userId: number,
    productsInStores: Basket[]
    totalPrice: number
}

export const EmptyOrder: OrderInfo = {
    orderId: -1,
    userId: -1,
    productsInStores: [],
    totalPrice: 0,
}