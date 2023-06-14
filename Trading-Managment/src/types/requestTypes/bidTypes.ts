export interface addBidParams {
    userId: number;
    storeId: number;
    price: Number;
    productId: number;
    quantity: number;
};
export interface answerBidParams {
    userId: number;
    storeId: number;
    answer: boolean;
    productId: number;
    bidId: number;
};
export interface counterBidParams {
    userId: number;
    storeId: number;
    offer: Number;
    productId: number;
    bidId: number;
};
export interface editBidParams {
    userId: number;
    storeId: number;
    bidId: number;
    price: Number;
    quantity: number;
};
