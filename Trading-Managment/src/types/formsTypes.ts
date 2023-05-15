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
export type fireManagerFormValues = {
    userId: number;
    storeId: number;
    managerToFireId: number;
};
export type fireOwnerFormValues = {
    userId: number;
    storeId: number;
    ownerToFireId: number;
}
export type appointManagerFormValues = {
    userId: number;
    storeId: number;
    emailOfManager: string;
}
