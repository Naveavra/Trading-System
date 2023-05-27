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
    token: string;
    message: string;
    userName: string;
}