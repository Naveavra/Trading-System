import { StoreRole } from "../systemTypes/StoreRole";

export interface TokenResponseBody {
    token: string;
    userId: number;
    userName: string;
    isAdmin: boolean;
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];

};
export interface RegisterResponseData {
    answer: string | null;
}
export interface EnterGuestResponseData {
    guestId: number;
}