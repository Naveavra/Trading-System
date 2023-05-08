export interface Message {
    id: number;
    content: string;
    rating: number;
    reviewer: number;
    orderId: number;
    storeId: number;
    productId: number;
    gotFeedBack: boolean;
    seen: boolean
}