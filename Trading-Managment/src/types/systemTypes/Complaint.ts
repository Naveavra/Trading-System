export interface Complaint {
    complaintId: number;
    opceode: number;
    content: string;
    userId: number;
    orderId: number;
    gotFeedback: boolean;
    seen: boolean;
};