import { Action } from "redux-deep-persist/lib/types";

export enum StoreRoleEnum {
    CREATOR = "Creator",
    OWNER = "Owner",
    MANAGER = "Manager",
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
    actions: Action[];
}