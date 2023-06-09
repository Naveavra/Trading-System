export interface SystemStatus {
    averagePurchase: number;
    averageRegistered: number;
    averageUserIn: number;
    averageUserOut: number;
};
export const emptySystemStatus: SystemStatus = {
    averagePurchase: 0,
    averageRegistered: 0,
    averageUserIn: 0,
    averageUserOut: 0,
};