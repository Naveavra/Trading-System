import { Bid } from "../systemTypes/Bid";
import { MyNotification } from "../systemTypes/Notification";
import { Order } from "../systemTypes/Order";
import { Permission } from "../systemTypes/Permission";
import { StoreRole, StoreName, StoreImg } from "../systemTypes/StoreRole";

export interface TokenResponseBody {
    token: string;
    userId: number;
    userName: string;
    isAdmin: boolean;
    age: number;
    birthday: string;
    notifications: MyNotification[];
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];
    storeNames: StoreName[];
    storeImgs: StoreImg[];
    permissions: Permission[];
    purchaseHistory: Order[];
    bids: Bid[];
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
    age: number;
    birthday: string;
    notifications: MyNotification[];
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];
    storeNames: StoreName[];
    storeImgs: StoreImg[];
    permissions: Permission[];
    purchaseHistory: Order[];
    bids: Bid[];
};
export interface getClientNotifications {
    notifications: string[];
};
