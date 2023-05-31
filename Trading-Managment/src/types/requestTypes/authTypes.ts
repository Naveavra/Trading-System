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
export interface editProfileParams {
    userId: number;
    name: string;
    email: string;
    age: number;
    birthday: string;
}