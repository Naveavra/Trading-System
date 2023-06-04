import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { LogRecord } from "../types/systemTypes/Log";
import { adminApi } from "../api/adminApi";
import { Complaint } from "../types/systemTypes/Complaint";
import { addAdminParams, answerComplaintParams } from "../types/requestTypes/adminTypes";
import { ApiError } from "../types/apiTypes";

interface AdminState {
    isLoading: boolean;
    error: string;
    logRecords: LogRecord[];
    complaints: Complaint[];
    msg: string;
}
const reducerName = 'adminSlice';
const initialState: AdminState = {
    logRecords: [],
    complaints: [],
    isLoading: false,
    error: '',
    msg: '',
}

export const getLogger = createAsyncThunk<
    LogRecord[],
    number,
    { rejectValue: ApiError }
>(
    `${reducerName}/getLogger`,
    async (adminId, thunkAPI) => {
        return adminApi.getLogger(adminId)
            .then((res) => thunkAPI.fulfillWithValue(res as LogRecord[]))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });
export const getComplaints = createAsyncThunk<
    Complaint[],
    number,
    { rejectValue: ApiError }
>(
    `${reducerName}/getComplaints`,
    async (adminId, thunkAPI) => {
        return adminApi.getComplaints(adminId)
            .then((res) => thunkAPI.fulfillWithValue(res as Complaint[]))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });

export const appointAdmin = createAsyncThunk<
    string,
    addAdminParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/appointAdmin`,
    async (adminParams, thunkAPI) => {
        return adminApi.appointAdmin(adminParams)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });
export const answerComplaint = createAsyncThunk<
    string,
    answerComplaintParams,
    { rejectValue: ApiError }
>(
    `${reducerName}/answerComplaint`,
    async (answerParams, thunkAPI) => {
        return adminApi.answerCompolaint(answerParams)
            .then((res) => thunkAPI.fulfillWithValue(res as string))
            .catch((err) => thunkAPI.rejectWithValue(err as ApiError));
    });



const { reducer: adminReducer, actions: authActions } = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        clearMsg: (state) => {
            state.msg = '';
        },
        clearError: (state) => {
            state.error = '';
        }
    },
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
            state.error = payload?.message.data ?? "error during get logger";
        });
        builder.addCase(appointAdmin.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(appointAdmin.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.msg = payload;
        });
        builder.addCase(appointAdmin.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during appoint admin";
        });
        builder.addCase(getComplaints.pending, (state) => {
            state.isLoading = true;
        });
        builder.addCase(getComplaints.fulfilled, (state, { payload }) => {
            state.isLoading = false;
            state.complaints = payload;
        });
        builder.addCase(getComplaints.rejected, (state, { payload }) => {
            state.isLoading = false;
            state.error = payload?.message.data ?? "error during get complaints";
        });
    }
});
export const { clearMsg, clearError } = authActions;

export default adminReducer;