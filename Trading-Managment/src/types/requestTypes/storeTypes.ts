export interface GetStoresParams {
    storeId: number,
}

export interface PostStoreParams {
    userId: number, 
    desc : string //description
}

export interface PatchStoreParams {
    userId: number, 
    storeId: number,
    desc: string,
    isActive: boolean
}

export interface DeleteStoreParams {
    userId: number,
    storeId: number
}

export interface GetStoreProducts {
    storeId: number
}