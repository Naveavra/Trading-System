import { Cart } from "../systemTypes/Cart"

export interface getOrderParams {
    userId: number
}

export interface postOrderParams {
    userId: number, 
    cart: Cart
}