export interface Order {
    orderId: number;
    userId: number;
    productsInStores: { storeId: number, basket: { productId: number, quantity: number }[] }[];
    totalPrice: number;
}