export interface GetStoresParams {
    userId: number,
    storeId: number,
}

export interface PostStoreParams {
    userId: number,
    name: string,
    img: string, //description
    desc: string //description
}

export interface PatchStoreParams {
    userId: number,
    storeId: number,
    desc: string | null,
    isActive: boolean | null
    img: string, //description
}

export interface DeleteStoreParams {
    userId: number,
    storeId: number
}

export interface GetStoreProducts {
    storeId: number
}

export interface AppointOwnerParams {
    storeId: number,
    userIncharge: number,
    newOwner: number
}

export interface getAppointmentsHistoryParams {
    storeId: number,
    userId: number
}