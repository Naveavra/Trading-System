export interface SystemStatus {
    averagePurchase: number;
    averageRegistered: number;
    averageUserIn: number;
    averageUserOut: number;
    memberCount: number;
    guestCount: number;
    purchaseCount: number;
    registeredCount: number;
};
export const emptySystemStatus: SystemStatus = {
    averagePurchase: 0,
    averageRegistered: 0,
    averageUserIn: 0,
    averageUserOut: 0,
    purchaseCount: 0,
    registeredCount: 0,
    memberCount: 0,
    guestCount: 0,
};