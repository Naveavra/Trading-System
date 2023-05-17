export interface ApiError {
    message: {
        data: string,
    };
};

export type ApiListData<T> = T[]



export type ApiResponseListData<T> = ApiListData<T> | ApiError;

export type ApiResponse<T> = T | ApiError;