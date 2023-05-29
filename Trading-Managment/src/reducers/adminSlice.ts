import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { LogRecord } from "../types/systemTypes/Log";
import { adminApi } from "../api/adminApi";

interface AdminState {
    isLoading: boolean;
    error: string;
    logRecords: LogRecord[];
}
const reducerName = 'adminSlice';
const initialState: AdminState = {
    logRecords: [],
    isLoading: false,
    error: '',
}

export const getLogger = createAsyncThunk<
    LogRecord[],
    number,
    { rejectValue: string }
>(
    `${reducerName}/getLogger`,
    async (adminId, thunkAPI) => {
        return adminApi.getLogger(adminId)
            .then((res) => thunkAPI.fulfillWithValue(res as LogRecord[]))
            .catch((err) => thunkAPI.rejectWithValue(err as string));
    });

const { reducer: adminReducer } = createSlice({
    name: reducerName,
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(getLogger.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(getLogger.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.logRecords = payload;
        });
        builder.addCase(getLogger.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload as string;
        });
    }
});

export default adminReducer;