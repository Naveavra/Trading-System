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
    desc: string
}

export interface DeleteStoreParams {
    userId: number,
    storeId: number
}