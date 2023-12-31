
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
export type addAdminFormValues = {
    userId: number;
    email: string;
    password: string;
};
export type answerComplaintFormValues = {
    adminId: number;
    complaintId: number;
    answer: string;
};
export interface answerQuestionFormValues {
    userId: number;
    storeId: number;
    questionId: number;
    answer: string;
}
export type paymentFormValues = {
    userId: number;
    cardNumber: string;
    month: string;
    year: string;
    holder: string;
    ccv: string;
    id: number;
    payment_service: string;
    //-----
    name: string;
    address: string;
    city: string;
    country: string;
    zip: string;
    supply_service: string;
};
export type supplyFormValues = {
    name: string;
    address: string;
    city: string;
    country: string;
    zip: string;
    supply_service: string;
};
export type closeStoreFormValues = {
    userId: number;
    storeId: number;
};
export type cancelMembershipFormValues = {
    userId: number;
    userName: string;
};
export type reviewOnStoreFormValues = {
    userId: number;
    storeName: string;
    orderId: number;
    rating: number;
    content: string;
};
export type reviewOnProductFormValues = {
    userId: number;
    storeId: number;
    orderId: number;
    productId: number;
    rating: number;
    content: string;
};
export type sendQuestionFormValues = {
    userId: number;
    storeId: number;
    question: string;
};
export type updateServiceFormValues = {
    userId: number;
    service: string;
    action: string;
    content: string;
}
export type makeBidFormValues = {
    userId: number;
    storeId: number;
    productId: number;
    price: Number;
    quantity: number;
}
export type answerBidFormValues = {
    userId: number;
    storeId: number;
    answer: boolean;
    productId: number;
    bidId: number;
}
export type counterBidFormValues = {
    userId: number;
    storeId: number;
    offer: Number;
    productId: number;
    bidId: number;
};
export type editBidFormValues = {
    userId: number;
    storeId: number;
    bidId: number;
    price: Number;
    quantity: number;
};
