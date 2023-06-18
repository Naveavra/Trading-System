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
export interface Question {
    questionId: number;
    messageId: number;
    opcode: number;
    storeId: number;
    userId: number;
    content: string;
    seen: boolean;
    gotFeedback: boolean;
}
export interface Review {
    storeReviewId: number;
    orderId: number;
    rating: number;
    messageId: number;
    opcode: number;
    storeId: number;
    userId: number;
    content: string;
    seen: boolean;
}

