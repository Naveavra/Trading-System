export interface LoginPostData {
    email: string;
    password: string;
};
export interface RegisterPostData {
    email: string;
    password: string;
    birthday: string;
};
export interface getUserData {
    userId: number;
    token: string;
};