
export interface StoreInfo {
    storeId: number;
    name: string;
    description: string;
    rating: number;
    isActive: boolean;
    creatorId: number;
    img: string;
    discounts: string[];
    purchasePolicies: string[];
}
export const emptyStoreInfo: StoreInfo = {
    storeId: 0,
    rating: 0,
    name: '',
    description: '',
    isActive: false,
    creatorId: 0,
    img: '',
    discounts: [],
    purchasePolicies: []
};