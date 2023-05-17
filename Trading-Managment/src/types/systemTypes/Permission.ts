import { Action } from "./Action";

export interface Permission {
    storeId: number,
    actions: Action[]
}