import {Basket} from '../systemTypes/Basket'

export interface GetCartParams {
    userId: number;
    baskets: Basket[];
}

export interface PostBasketParams {
    userId: number;
    storeId: number;
    baket: Basket;
}

export interface DeleteCartParams{
    userId: number;
} 

export interface PatchCartParams{
    userId: number;
    storeId: number;
    prouctId: number;
    quantity: number;
}
