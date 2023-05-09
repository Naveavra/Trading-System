import { Permission } from "../systemTypes/Permission";
import { StoreRole } from "../systemTypes/StoreRole";

export interface TokenResponseBody {
    token: string;
    userId: number;
    userName: string;
    isAdmin: boolean;
    notifications: string[];
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];
    permmisions: Permission[];
};
export interface RegisterResponseData {
    answer: string | null;
}
export interface EnterGuestResponseData {
    guestId: number;
}
