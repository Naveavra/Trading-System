export interface Message {
    messageId: number;
    state: string;
    content: string;
    rating: number;
    reviewer: number;
    orderId: number;
    storeId: number;
    productId: number;
    gotFeedBack: boolean;
    seen: boolean
}