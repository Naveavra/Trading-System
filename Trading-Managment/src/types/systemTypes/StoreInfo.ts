
export interface StoreInfo {
    id: number;
    name: string;
    description: string;
    isActive: boolean;
    creatorId: number;
    img: string;
}
export const emptyStoreInfo: StoreInfo = {
    id: 0,
    name: '',
    description: '',
    isActive: false,
    creatorId: 0,
    img: '',
}