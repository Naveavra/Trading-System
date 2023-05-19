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
    storeName: string;
    storeId: number;

};
export interface StoreImg {
    storeImg: string;
    storeId: number;
}
export interface RoleInStore {
    userId: number;
    userName: string;
    storeRole: StoreRoleEnum;
}