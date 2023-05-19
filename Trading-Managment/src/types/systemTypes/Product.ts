export interface Product {
    productId: number;
    categories: string[];
    storeId: number;
    name: string;
    description: string;
    price: number;
    quantity: number;
    img: string;
    rating: number;
    revies: { value: number, content: string }[];
}
export const EmptyProduct: Product = {
    productId: -1,
    categories: [],
    name: "",
    description: "",
    price: 0,
    quantity: 0,
    img: "",
    rating: 0,
    revies: [],
    storeId: 0
}