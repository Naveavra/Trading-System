export interface Basket {
    storeId: number,
    products: { quantity: number, productId: number }[]
}
