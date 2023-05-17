import { Basket } from '../systemTypes/Basket'

export interface GetCartParams {
    userId: number;
}

export interface PostBasketParams {
    userId: number;
    storeId: number;
    basket: Basket;
}

export interface DeleteCartParams {
    userId: number;
}

export interface PatchCartParams {
    userId: number;
    storeId: number;
    productId: number;
    quantity: number;
}
export interface buyCartParams {
    userId: number;
    accountNumber: string;
}
