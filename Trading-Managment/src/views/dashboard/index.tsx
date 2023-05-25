import * as React from 'react';

import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import AlertDialog from '../../components/Dialog/AlertDialog';
import { clearAuthError, getClientData, getNotifications } from '../../reducers/authSlice';
import CartLogo from '../../components/Loaders/cartLoader';
import { clearStoresError, clearStoresResponse, getStore, getStoresInfo } from '../../reducers/storesSlice';
import axios from 'axios';
import { Outlet } from 'react-router-dom';
import { clearProductsError, getProducts } from '../../reducers/productsSlice';
import { Divider } from '@mui/material';
import Bar from '../../components/Bars/Navbar/Navbar';
import { SearchBar } from '../../components/Bars/SearchBar/SearchBar';
import ShopsBar from '../../components/Bars/ShopBar/ShopBar';
import Categories2 from '../../components/Categories/category2';
import Products from '../../components/Product/Products';
import { getCart } from '../../reducers/cartSlice';
import SuccessAlert from '../../components/Alerts/success';

const DashboardPage: React.FC = () => {
    const dispatch = useAppDispatch();
    const [text, setText] = useState('');
    const isLoadingShops = useAppSelector((state) => !!state.store.isLoading);
    const isLoadingProducts = useAppSelector((state) => !!state.product.isLoading);

    const userId = useAppSelector((state) => state.auth.userId);
    const token = useAppSelector((state) => state.auth.token) ?? "";
    const userName = useAppSelector((state) => state.auth.userName);
    const isAdmin = useAppSelector((state) => state.auth.isAdmin);
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    //const opcode = useAppSelector((state) => state.auth.opcode);
    const error = useAppSelector((state) => state.auth.error);
    const shopError = useAppSelector((state) => state.store.error);
    const productError = useAppSelector((state) => state.product.error);

    //success alerts
    const openStoreAlert = useAppSelector((state) => state.store.storeState.responseData);


    const PING_INTERVAL = 10000; // 10 seconds in milliseconds

    // Send a ping to the server
    const sendPing = () => {
        if (userId != 0) {
            axios.post('http://localhost:4567/api/auth/ping', { userId: userId })
                .then(response => {
                    // Do something with the response if necessary
                })
                .catch(error => {
                    // Handle the error if necessary
                });
            // dispatch(ping(userId));
        }
    }
    const fetchNotification = async () => {
        try {
            console.log("trying get notification")
            if (token != "" && userName != 'guest') {
                const response = await dispatch(getNotifications({ userId: userId, token: token }));
                if (response.payload?.opcode >= 0 && response.payload?.opcode <= 6) {
                    dispatch(getStore({ userId: userId, storeId: storeId }));
                }
                else if (!isAdmin && ((response.payload?.opcode >= 7 && response.payload?.opcode <= 12) || response.payload?.opcode == 14 || response.payload?.opcode == 15)) {
                    dispatch(getClientData({ userId: userId }));
                }
                if (isAdmin && (response.payload?.opcode == 14 || response.payload?.opcode == 13)) {
                    //dispatch(getAdminData());
                }
                fetchNotification();
            }
        } catch (error) {
            console.error('Error fetching notification:', error);
        }
    };
    useEffect(() => {
        // Call the sendPing function every 2 seconds
        const pingInterval = setInterval(sendPing, PING_INTERVAL);

        dispatch(getStoresInfo());
        dispatch(getProducts());
        dispatch(getCart({ userId: userId }));
        // Stop the ping interval when the user leaves the app
        //---------------------notifications---------------------

        fetchNotification();
        return () => {
            clearInterval(pingInterval)
        };
    }, [userId])
    const handleSet = (text: string) => {
        setText(text);
    }
    //for each product check  for ech category if text is in the category


    return (
        <>
            {isLoadingShops || isLoadingProducts ?
                <CartLogo />
                : !!error ? <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()) }} text={error} sevirity={'error'} />
                    : !!shopError ? <AlertDialog open={!!shopError} onClose={() => { dispatch(clearStoresError()) }} text={shopError} sevirity={'error'} />
                        : !!productError ? <AlertDialog open={!!productError} onClose={() => { dispatch(clearProductsError()) }} text={productError} sevirity={'error'} />
                            : <>
                                <Bar headLine={"Trading System"} />
                                <SearchBar text={text} set={handleSet} />
                                <Divider sx={{ marginTop: 1 }} />
                                {openStoreAlert ? <SuccessAlert message={openStoreAlert} onClose={() => { dispatch(clearStoresResponse({})) }} /> : null}
                                <ShopsBar />
                                <Divider />
                                {/* <Categories /> */}
                                <Categories2 />
                                <Divider />
                                <Products text={text} />
                                <Outlet />
                            </>
            }

            <Outlet />

        </>


    )
};

export default DashboardPage;