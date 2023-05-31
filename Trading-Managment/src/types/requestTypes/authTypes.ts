export interface LoginPostData {
    email: string;
    password: string;
};
export interface RegisterPostData {
    email: string;
    password: string;
    birthday: string;
};
export interface getUserNotifications {
    userId: number;
    token: string;
};
export interface getUserData {
    userId: number;

};
export interface messageParams {
    userId: number;
    message: string;
    userName: string;
}
export interface complaintParams {
    userId: number;
    orderId: number;
    complaint: string;
}
export interface editProfileParams {
    userId: number;
    email: string;
    birthday: string;
}
export interface changePasswordParams {
    userId: number;
    oldPassword: string;
    newPassword: string;
}