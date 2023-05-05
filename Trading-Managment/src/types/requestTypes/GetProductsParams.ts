export interface GetProductsParams {
    id: number,
};
export interface PostProductsParams {
    id: number;
    storeId: number;
    category: string[];
    name: string;
    description: string;
    price: number;
    quantity: number;
    img: string;
    
}
export interface PatchCustomerParams {
    id: number;
    storeId: number;
    category?: string[];
    name?: string;
    description?: string;
    price?: number;
    quantity?: number;
    img?: string;
    rating?: Number;
}