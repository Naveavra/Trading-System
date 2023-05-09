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