import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError } from "../types/apiTypes";
import { TokenResponseBody, RegisterResponseData, EnterGuestResponseData } from "../types/responseTypes/authTypes";
import { LoginFormValues } from "../views/LoginPage/types";
import { authApi } from "../api/authApi";
import { localStorage } from '../config'
import { RegisterPostData } from "../types/requestTypes/authTypes";
import { StoreRole } from "../types/systemTypes/StoreRole";

interface AuthState {
    token: string | null;
    userId: number;
    userName: string;
    isAdmin: boolean;
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];
    error: string | null;
    isLoginLoading: boolean;
    isRegisterLoading: boolean;
}
const reducrName = 'auth';
const initialState: AuthState = {
    token: window.localStorage.getItem(localStorage.auth.token.name) || '',
    userId: parseInt(window.localStorage.getItem(localStorage.auth.userId.name) || '0'),
    userName: window.localStorage.getItem(localStorage.auth.userName.name) || '',
    isAdmin: window.localStorage.getItem(localStorage.auth.isAdmin.name) === 'true' ? true : false,
    hasQestions: false,
    storeRoles: [],
    message: null,
    error: null,
    isLoginLoading: false,
    isRegisterLoading: false,
};

export const login = createAsyncThunk<
    { rememberMe: boolean, responseBody: TokenResponseBody },
    LoginFormValues,
    { rejectValue: ApiError }
>(
    `${reducrName}/login`,
    async (formData, thunkApi) => {
        const { rememberMe, ...credentials } = formData;
        return authApi.login(credentials)
            .then((res) => thunkApi.fulfillWithValue({
                rememberMe: rememberMe,
                responseBody: res as TokenResponseBody
            }))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

export const register = createAsyncThunk<
    { responseBody: RegisterResponseData },
    RegisterPostData,
    { rejectValue: ApiError }
>(
    `${reducrName}/register`,
    async (formData, thunkApi) => {
        const { ...credentials } = formData;
        return authApi.register(credentials)
            .then((res) => thunkApi.fulfillWithValue({
                responseBody: res as RegisterResponseData
            }))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

//guest enter
export const guestEnter = createAsyncThunk<
    { responseBody: EnterGuestResponseData },
    void,
    { rejectValue: ApiError }
>(
    `${reducrName}/guestEnter`,
    async (_, thunkApi) => {
        return authApi.guestEnter()
            .then((res) => thunkApi.fulfillWithValue({
                responseBody: res as EnterGuestResponseData
            }))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
//ping
export const ping = createAsyncThunk<
    { responseBody: string },
    number,
    { rejectValue: ApiError }
>(
    `${reducrName}/ping`,
    async (credential, thunkApi) => {
        return authApi.ping(credential)
            .then((res) => thunkApi.fulfillWithValue({
                responseBody: res as string
            }))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });


const { reducer: authReducer, actions: authActions } = createSlice({
    name: reducrName,
    initialState,
    reducers: {
        logout: (state) => {
            state.token = null;
            state.userId = -1;
            state.userName = "";
            window.localStorage.removeItem(localStorage.auth.token.name);
            window.localStorage.removeItem(localStorage.auth.userId.name);
            window.localStorage.removeItem(localStorage.auth.userName.name);
            window.localStorage.removeItem(localStorage.auth.isAdmin.name);
        },
        clearAuthError: (state) => {
            state.error = null;
        },
        clearAuthMsg: (state) => {
            state.message = null;
        }
    },
    extraReducers: builder => {
        //login
        builder.addCase(login.pending, (state) => {
            state.isLoginLoading = true;
            state.error = null;
        });
        builder.addCase(login.fulfilled, (state, { payload }: PayloadAction<{ rememberMe: boolean, responseBody: TokenResponseBody }>) => {
            state.isLoginLoading = false;
            state.token = payload.responseBody.token;
            state.userId = payload.responseBody.userId;
            state.userName = payload.responseBody.userName;
            console.log(payload.responseBody);
            if (payload.rememberMe) {
                window.localStorage.setItem(localStorage.auth.token.name, payload.responseBody.token);
                window.localStorage.setItem(localStorage.auth.userId.name, payload.responseBody.userId.toString());
                window.localStorage.setItem(localStorage.auth.userName.name, payload.responseBody.userName);
                window.localStorage.setItem(localStorage.auth.isAdmin.name, payload.responseBody.isAdmin.toString());
            }
            else {
                window.localStorage.removeItem(localStorage.auth.token.name);
                window.localStorage.removeItem(localStorage.auth.userId.name);
                window.localStorage.removeItem(localStorage.auth.userName.name);
                window.localStorage.removeItem(localStorage.auth.isAdmin.name);
            }
        });
        builder.addCase(login.rejected, (state, { payload }) => {
            console.log("payload", payload);
            state.isLoginLoading = false;
            state.error = payload?.message.data.errorMsg ?? "error during login";
        });
        //register
        builder.addCase(register.pending, (state) => {
            state.isRegisterLoading = true;
            state.error = null;
        });
        builder.addCase(register.fulfilled, (state, { payload }: PayloadAction<{ responseBody: RegisterResponseData }>) => {
            state.isRegisterLoading = false;
            state.message = payload.responseBody.answer;
        });
        builder.addCase(register.rejected, (state, { payload }) => {
            state.isRegisterLoading = false;
            state.error = payload?.message.data.errorMsg ?? "error during register";
        });
        //guest enter
        builder.addCase(guestEnter.pending, (state) => {
            state.isLoginLoading = true;
            state.error = null;
        });
        builder.addCase(guestEnter.fulfilled, (state, { payload }: PayloadAction<{ responseBody: EnterGuestResponseData }>) => {
            state.isLoginLoading = false;
            state.token = null;
            state.userId = payload.responseBody.guestId;
            state.userName = "guest";
        });
        builder.addCase(guestEnter.rejected, (state, { payload }) => {
            console.log("guestEnter.rejected", payload);
            state.isLoginLoading = false;
            state.error = payload?.message.data.errorMsg ?? "error during guest enter";
        });
    }
});
// Action creators are generated for each case reducer function
export const { logout, clearAuthError, clearAuthMsg } = authActions;
export default authReducer;
