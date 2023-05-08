import { Cart } from "./Cart";

export interface Order 
{
    orderId: number;
    cart: Cart;
}