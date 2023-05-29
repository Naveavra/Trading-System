import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../reducers/authSlice';
import storeReducer from '../reducers/storesSlice';
import productsReducer from '../reducers/productsSlice';
import cartReducer from "../reducers/cartSlice";
import storage from 'redux-persist/lib/storage';
import { persistReducer, persistStore } from 'redux-persist';
import storageSession from 'reduxjs-toolkit-persist/lib/storage/session'
import orderReducer from '../reducers/orderSlice';
import { getPersistConfig } from 'redux-deep-persist';
import discountReducer from '../reducers/discountSlice';
import adminReducer from '../reducers/adminSlice';


const authPersistConfig = {
    key: 'auth',
    storage: storageSession,
    whitelist: ['token', 'isAuthenticated', 'userId', 'userName'],
};
const persistedAuthReducer = persistReducer(authPersistConfig, authReducer)

const storePersistConfig = getPersistConfig({
    key: 'store',
    storage: storageSession,
    whitelist: ['storeState.watchedStore.storeId'],
    rootReducer: storeReducer,
});
const persistedStoreReducer = persistReducer(storePersistConfig, storeReducer)
// const persistedProductsReducer = persistReducer(persistConfig, productsReducer)
// const persistedCartReducer = persistReducer(persistConfig, cartReducer)

export const store = configureStore({
    reducer: {
        auth: persistedAuthReducer,
        store: persistedStoreReducer,
        product: productsReducer,
        cart: cartReducer,
        order: orderReducer,
        discount: discountReducer,
        admin: adminReducer,

    },
    middleware(getDefaultMiddleware) {
        return getDefaultMiddleware({
            serializableCheck: false,
        });
    },
    devTools: process.env.NODE_ENV !== 'production',
});


export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;
export const useAppDispatch: () => AppDispatch = useDispatch;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
export const persistor = persistStore(store)