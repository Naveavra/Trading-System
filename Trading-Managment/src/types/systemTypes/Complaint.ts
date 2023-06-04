export interface Complaint {
    messageId: number;
    opceode: number;
    content: string;
    userId: number;
    orderId: number;
    gotFeedback: boolean;
    seen: boolean;
};