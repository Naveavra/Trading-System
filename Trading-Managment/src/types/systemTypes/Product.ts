export interface Product {
    id: number;
    storeId: number;
    category: string[];
    name: string;
    description: string;
    price: number;
    quantity: number;
    img: string;
    rating: { value: number, content: string }[];
    reviewNumber: number;
}
export const EmptyProduct : Product = {
    id : -1,
    storeId: 0,
    category: [],
    name: "",
    description: "",
    price: 0,
    quantity: 0,
    img: "",
    rating: [],
    reviewNumber: 0,
}