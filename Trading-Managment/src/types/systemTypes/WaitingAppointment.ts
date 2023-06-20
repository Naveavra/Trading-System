import { StoreRoleEnum } from "./StoreRole";

export interface WaitingAppointment {
    memane: string;
    memune: string;
    role: StoreRoleEnum;
    storeId: number;
    approved: string[];
    notApproved: string[];
    status: boolean;
}