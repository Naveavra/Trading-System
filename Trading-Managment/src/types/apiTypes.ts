export interface ApiError {
    message: {
        data: { errorMsg: string },
        status: number,
    };
};

export interface ApiListData<T> {
    errorOccured: boolean,
    errorMsg: string,
    value: {
        count: number,
        results: T[]
    }
}
export type ValidationError = {
    message: {
        key: string[],
        value: string[],
    }
}

export type ApiResponseListData<T> = ApiListData<T> | ApiError;

export type ApiResponse<T> = T | ApiError;  