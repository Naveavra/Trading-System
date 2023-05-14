import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../reducers/authSlice';
import storeReducer from '../reducers/storesSlice';
import productsReducer from '../reducers/productsSlice';
import cartReducer from "../reducers/cartSlice";

export const store = configureStore({
    reducer: {
        auth: authReducer,
        store:storeReducer,
        product:productsReducer,
        cart:cartReducer,
        
    },
    middleware(getDefaultMiddleware) {
        return getDefaultMiddleware({
            serializableCheck: false,
        });
    },
});


export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;
export const useAppDispatch: () => AppDispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;