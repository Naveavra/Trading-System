export type ProductFormValues = {
    id: number;
    storeId: number;
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
