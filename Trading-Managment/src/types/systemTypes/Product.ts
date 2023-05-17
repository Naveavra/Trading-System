export interface Product {
    productId: number;
    categories: string[];
    storeId: number;
    name: string;
    description: string;
    price: number;
    quantity: number;
    img: string;
    rating: { value: number, content: string }[];
    reviewNumber: number;
}
export const EmptyProduct: Product = {
    productId: -1,
    categories: [],
    name: "",
    description: "",
    price: 0,
    quantity: 0,
    img: "",
    rating: [],
    reviewNumber: 0,
    storeId: 0
}