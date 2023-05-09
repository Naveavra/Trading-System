export interface ApiError {
    message: {
        data: string,
    };
};

export interface ApiListData<T> {
    data: {
        results: T[]
    }
}


export type ApiResponseListData<T> = ApiListData<T> | ApiError;

export type ApiResponse<T> = T | ApiError;