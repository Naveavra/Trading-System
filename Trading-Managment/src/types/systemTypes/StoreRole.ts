export enum StoreRoleEnum {
    CREATOR,
    OWNER,
    MANAGER,
};

export interface StoreRole {
    storeId: number;
    storeRole: StoreRoleEnum;
};
export interface StoreName {
    storeId: number;
    storeName: string;

};
export interface StoreImg {
    storeId: number;
    sroteImg: string;
}