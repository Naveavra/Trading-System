export interface answerComplaintParams {
    adminId: number;
    complaintId: number;
    answer: string;
};
export interface addAdminParams {
    userId: number;
    email: string;
    password: string;
};
export interface closeStorePerminentlyParams {
    userId: number;
    storeId: number;
};
export interface cancelMembershipParams {
    userId: number;
    userName: string;
};
export interface updateServiceParams {
    userId: number;
    service: string;
    action: string;
    content: string;
};