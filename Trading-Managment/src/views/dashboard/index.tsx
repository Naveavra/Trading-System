import * as React from 'react';

import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import AlertDialog from '../../components/Dialog/AlertDialog';
import { clearAuthError, getClientData } from '../../reducers/authSlice';
import CartLogo from '../../components/Loaders/cartLoader';
import { clearStoresError, getStoresInfo } from '../../reducers/storesSlice';
import axios from 'axios';
import { Outlet } from 'react-router-dom';
import { clearProductsError, getProducts } from '../../reducers/productsSlice';
import { Divider } from '@mui/material';
import Bar from '../../components/Bars/Navbar/Navbar';
import { SearchBar } from '../../components/Bars/SearchBar/SearchBar';
import ShopsBar from '../../components/Bars/ShopBar/ShopBar';
import Categories2 from '../../components/Categories/category2';
import ProductCard from '../../components/ProductCard/Card';
import { Product } from '../../types/systemTypes/Product';

const DashboardPage: React.FC = () => {
    const dispatch = useAppDispatch();

    const [text, setText] = useState('');
    const isLoadingShops = useAppSelector((state) => !!state.store.isLoading);
    const isLoadingProducts = useAppSelector((state) => !!state.product.isLoading);
    const userId = useAppSelector((state) => state.auth.userId);
    const token = useAppSelector((state) => state.auth.token) ?? "";
    const error = useAppSelector((state) => state.auth.error);
    const shopError = useAppSelector((state) => state.store.error);
    const productError = useAppSelector((state) => state.product.error);
    const response = useAppSelector((state) => state.product.responseData);
    const products = response?.data?.results ?? [];
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds
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
    useEffect(() => {
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        dispatch(getStoresInfo());
        dispatch(getProducts());
        if (token != "") {
            dispatch(getClientData({ userId: userId, token: token }));
        }
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };

    }, [dispatch]);
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
                                <ShopsBar />
                                <Divider />
                                {/* <Categories /> */}
                                <Categories2 />
                                {products ? products.filter((product) => product.categories.reduce((acc, curr) => acc || curr.includes(text), false) || product.description.includes(text) || product.name.includes(text)).map((product: Product) => {
                                    <ProductCard item={product} key={product.productId} canEdit={false} canDelete={false} />
                                }) : null}
                                <Outlet />
                            </>
            }
            <Outlet />
        </>


    )
};

export default DashboardPage;