
export interface GetCartParams {
    userId: number;
}

// export interface PostBasketParams {
//     userId: number;
//     storeId: number;
//     basket: Basket;
// }

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
    cardNumber: string;
    month: string;
    year: string;
    holder: string;
    ccv: string;
    id: number;
    payment_service: string;
    //-----
    name: string;
    address: string;
    city: string;
    country: string;
    zip: string;
    supply_service: string;
}
