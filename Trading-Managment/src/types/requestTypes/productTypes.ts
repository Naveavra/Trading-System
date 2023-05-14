export interface GetStoreProductsParams {
    storeId: number
};
export interface PostProductsParams {

    id: number; //userid
    storeId: number;
    category: string[];
    name: string;
    description: string;
    price: number;
    quantity: number;
    img: string;

}
export interface PatchProductsParams {
    id: number; //userid
    storeId: number;
    productId: number;
    category: string[]| null;
    name: string | null;
    description: string | null;
    price: number | null;
    quantity: number| null;
    img: string | null;
}
export interface DeleteProductsParams {
    id: number; //userid
    storeId: number;
    productId: number;
}