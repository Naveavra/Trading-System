export interface ApiError {
    message: {
        data: { errorMsg: string },
    };
};

export interface ApiListData<T> {
    value: {
        count: number,
        results: T[]
    }
}


export type ApiResponseListData<T> = ApiListData<T> | ApiError;

export type ApiResponse<T> = T | ApiError;