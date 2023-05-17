import { Permission } from "../systemTypes/Permission";
import { StoreRole, StoreName, StoreImg } from "../systemTypes/StoreRole";

export interface TokenResponseBody {
    token: string;
    userId: number;
    userName: string;
    isAdmin: boolean;
    notifications: string[];
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];
    storeNames: StoreName[];
    storeImgs: StoreImg[];
    permissions: Permission[];
};
export interface RegisterResponseData {
    answer: string | null;
}
export interface EnterGuestResponseData {
    guestId: number;
}
export interface getClientResponseData {
    userName: string;
    isAdmin: boolean;
    notifications: string[];
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];
    storeNames: StoreName[];
    storeImgs: StoreImg[];
    permissions: Permission[];
};
