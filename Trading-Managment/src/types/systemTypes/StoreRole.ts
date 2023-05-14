export enum StoreRoleEnum {
    CREATOR,
    OWNER,
    MANAGER,
};

export interface StoreRole {
    storeId: number;
    storeName: string;
    storeRole: StoreRoleEnum;
};