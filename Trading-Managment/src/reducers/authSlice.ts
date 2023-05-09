import { PayloadAction, createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ApiError } from "../types/apiTypes";
import { TokenResponseBody, RegisterResponseData, EnterGuestResponseData } from "../types/responseTypes/authTypes";
import { LoginFormValues } from "../views/LoginPage/types";
import { authApi } from "../api/authApi";
import { localStorage } from '../config'
import { RegisterPostData } from "../types/requestTypes/authTypes";
import { StoreRole } from "../types/systemTypes/StoreRole";
import { log } from "console";
import { Permission } from "../types/systemTypes/Permission";

interface AuthState {
    token: string | null;
    userId: number;
    userName: string;
    isAdmin: boolean;
    notifications: string[];
    message: string | null;
    hasQestions: boolean;
    storeRoles: StoreRole[];
    permmisions: Permission[];
    error: string | null;
    isLoginLoading: boolean;
    isRegisterLoading: boolean;
    isLogoutLoading: boolean;
}
const reducrName = 'auth';
const initialState: AuthState = {
    token: window.localStorage.getItem(localStorage.auth.token.name) || '',
    userId: parseInt(window.localStorage.getItem(localStorage.auth.userId.name) || '0'),
    userName: window.localStorage.getItem(localStorage.auth.userName.name) || '',
    //todo : add get function to all of the this
    // isAdmin: window.localStorage.getItem(localStorage.auth.isAdmin.name) === 'true' ? true : false,
    // hasQestions: window.localStorage.getItem(localStorage.auth.hasQestions.name) === 'true' ? true : false,
    // storeRoles: JSON.parse(window.localStorage.getItem(localStorage.auth.storeRoles.name) || '[]'),
    // notifications: JSON.parse(window.localStorage.getItem(localStorage.auth.notifications.name) || '[]'),
    // permmisions: JSON.parse(window.localStorage.getItem(localStorage.auth.permmisions.name) || '[]'),
    isAdmin: false,
    hasQestions: false,
    storeRoles: [],
    notifications: [],
    permmisions: [],
    message: null,
    error: null,
    isLoginLoading: false,
    isRegisterLoading: false,
    isLogoutLoading: false,
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
    string,
    RegisterPostData,
    { rejectValue: ApiError }
>(
    `${reducrName}/register`,
    async (formData, thunkApi) => {
        const { ...credentials } = formData;
        return authApi.register(credentials)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });
//logout
export const logout = createAsyncThunk<
    string,
    number,
    { rejectValue: ApiError }
>(
    `${reducrName}/logout`,
    async (userId, thunkApi) => {
        return authApi.logout(userId)
            .then((res) => thunkApi.fulfillWithValue(res as string))
            .catch((res) => thunkApi.rejectWithValue(res as ApiError))
    });

//guest enter
export const guestEnter = createAsyncThunk<
    number,
    void,
    { rejectValue: ApiError }
>(
    `${reducrName}/guestEnter`,
    async (_, thunkApi) => {
        return authApi.guestEnter()
            .then((res) => thunkApi.fulfillWithValue(res as number))
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
        clearAuthError: (state) => {
            state.error = null;
        },
        clearAuthMsg: (state) => {
            state.message = null;
        },

    },
    extraReducers: builder => {
        //login
        builder.addCase(login.pending, (state) => {
            state.isLoginLoading = true;
            state.error = null;
        });
        builder.addCase(login.fulfilled, (state, { payload }: PayloadAction<{ rememberMe: boolean, responseBody: TokenResponseBody }>) => {
            state.isLoginLoading = false;
            console.log(payload);
            state.token = payload.responseBody.token;
            state.userId = payload.responseBody.userId;
            state.userName = payload.responseBody.userName;
            state.isAdmin = payload.responseBody.isAdmin;
            state.hasQestions = payload.responseBody.hasQestions;
            state.storeRoles = payload.responseBody.storeRoles;
            state.notifications = payload.responseBody.notifications;
            state.permmisions = payload.responseBody.permmisions;
            console.log(payload.responseBody);
            if (payload.rememberMe) {
                window.localStorage.setItem(localStorage.auth.token.name, payload.responseBody.token);
                window.localStorage.setItem(localStorage.auth.userId.name, payload.responseBody.userId.toString());
                window.localStorage.setItem(localStorage.auth.userName.name, payload.responseBody.userName);
                // window.localStorage.setItem(localStorage.auth.isAdmin.name, payload.responseBody.isAdmin.toString());
                // window.localStorage.setItem(localStorage.auth.hasQestions.name, payload.responseBody.hasQestions.toString());
                // window.localStorage.setItem(localStorage.auth.storeRoles.name, JSON.stringify(payload.responseBody.storeRoles));
                // window.localStorage.setItem(localStorage.auth.notifications.name, JSON.stringify(payload.responseBody.notifications));
            }
            else {
                window.localStorage.removeItem(localStorage.auth.token.name);
                window.localStorage.removeItem(localStorage.auth.userId.name);
                window.localStorage.removeItem(localStorage.auth.userName.name);
                // window.localStorage.removeItem(localStorage.auth.isAdmin.name);
                // window.localStorage.removeItem(localStorage.auth.hasQestions.name);
                // window.localStorage.removeItem(localStorage.auth.storeRoles.name);
                // window.localStorage.removeItem(localStorage.auth.notifications.name);
            }
        });
        builder.addCase(login.rejected, (state, { payload }) => {
            state.isLoginLoading = false;
            state.error = payload?.message.data ?? "error during login";
        });
        //register
        builder.addCase(register.pending, (state) => {
            state.isRegisterLoading = true;
            state.error = null;
        });
        builder.addCase(register.fulfilled, (state, { payload }) => {
            console.log("reg payload", payload)
            state.isRegisterLoading = false;
            state.message = payload;
        });
        builder.addCase(register.rejected, (state, { payload }) => {
            state.isRegisterLoading = false;
            state.error = payload?.message.data ?? "error during register";
            state.message = null;
        });
        //logout
        builder.addCase(logout.pending, (state) => {
            state.isLogoutLoading = true;
            state.error = null;
        });
        builder.addCase(logout.fulfilled, (state, { payload }) => {
            state.isLogoutLoading = false;
            state.token = null;
            state.userId = 0;
            state.userName = '';
            window.localStorage.removeItem(localStorage.auth.token.name);
            window.localStorage.removeItem(localStorage.auth.userId.name);
            window.localStorage.removeItem(localStorage.auth.userName.name);
            // window.localStorage.removeItem(localStorage.auth.isAdmin.name);
        });
        builder.addCase(logout.rejected, (state, { payload }) => {
            state.isLogoutLoading = false;
            state.error = payload?.message.data ?? "error during logout";
        });
        //guest enter
        builder.addCase(guestEnter.pending, (state) => {
            state.isLoginLoading = true;
            state.error = null;
        });
        builder.addCase(guestEnter.fulfilled, (state, { payload }) => {
            state.isLoginLoading = false;
            state.token = null;
            state.userId = payload;
            state.userName = "guest";
        });
        builder.addCase(guestEnter.rejected, (state, { payload }) => {
            console.log("guestEnter.rejected", payload);
            state.isLoginLoading = false;
            state.error = payload?.message.data ?? "error during guest enter";
        });
    }
});
// Action creators are generated for each case reducer function
export const { clearAuthError, clearAuthMsg } = authActions;
export default authReducer;
