export interface Basket {
    storeid: number;
    productsList: {productId: number, quantity: number}[]
}