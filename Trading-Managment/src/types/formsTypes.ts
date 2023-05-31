export type ProductFormValues = {
    id: number;
    storeId: number;
    productId: number;
    category: string[];
    name: string;
    description: string;
    price: number;
    quantity: number;
    img: string;
};

export type fireUserFormValues = {
    userId: number;
    storeId: number;
    userToFire: number;
}
export type appointUserFormValues = {
    userId: number;
    storeId: number;
    emailOfUser: string;
}
export type storeFormValues = {
    userId: number,
    storeId: number,
    isActive: boolean,
    name: string,
    desc: string,
    img: string, //description
}
export type sendMsgFormValues = {
    userId: number;
    message: string;
    userName: string;
}
export type editProfileFormValues = {
    userId: number;
    name: string;
    email: string;
    age: number;
    birthday: string;
}
export type changePasswordFormValues = {
    userId: number;
    oldPassword: string;
    newPassword: string;
}
export type sentComplaintFormValues = {
    userId: number;
    orderId: number;
    complaint: string;
};