export interface Order {
    orderId: number;
    userId: number;
    products: { storeId: number, basket: { productId: number, quantity: number }[] }[];
    totalPrice: number;
}