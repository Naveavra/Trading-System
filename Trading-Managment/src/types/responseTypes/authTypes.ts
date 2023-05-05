
export interface TokenResponseBody {
    token: string;
    userId: number;
    userName: string;
};
export interface RegisterResponseData {
    answer: string | null;
}
export interface EnterGuestResponseData {
    guestId: number;
}