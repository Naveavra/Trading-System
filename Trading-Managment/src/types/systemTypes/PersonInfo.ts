import { Action } from "./Action";
import { StoreRoleEnum } from "./StoreRole";

export interface PersonInfo {
    birthday: string;
    role: StoreRoleEnum;
    managerPermissions: Action[];
    userName: string;
    userId: number;
    email: string;
    age: number;
}