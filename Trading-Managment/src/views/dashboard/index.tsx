import * as React from 'react';

import { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import DashboardFrame from '../../components/Frame';
import AlertDialog from '../../components/Dialog/AlertDialog';
import { clearAuthError } from '../../reducers/authSlice';
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
import { products } from '../../mock/products';
import { Product } from '../../types/systemTypes/Product';

const DashboardPage: React.FC = () => {
    const dispatch = useAppDispatch();
    const isLoadingShops = useAppSelector((state) => !!state.store.isLoading);
    const isLoadingProducts = useAppSelector((state) => !!state.product.isLoading);
    const userId = useAppSelector((state) => state.auth.userId);
    const error = useAppSelector((state) => state.auth.error);
    const shopError = useAppSelector((state) => state.store.error);
    const productError = useAppSelector((state) => state.product.error);
    const response = useAppSelector((state) => state.product.responseData);
    const products = response?.data?.results ?? [];
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds
    const sendPing = () => {
        console.log("pinging", userId);
        if (userId != 0) {
            console.log("ping", userId);
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
        console.log("mount");
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        dispatch(getStoresInfo());
        dispatch(getProducts());
        // Stop the ping interval when the user leaves the app
        return () => {
            console.log("unmount");
            clearInterval(pingInterval)
        };

    }, [dispatch]);
    return (
        <>
            {isLoadingShops || isLoadingProducts ?
                <CartLogo />
                : !!error ? <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()) }} text={error} sevirity={'error'} />
                    : !!shopError ? <AlertDialog open={!!shopError} onClose={() => { dispatch(clearStoresError()) }} text={shopError} sevirity={'error'} />
                        : !!productError ? <AlertDialog open={!!productError} onClose={() => { dispatch(clearProductsError()) }} text={productError} sevirity={'error'} />
                            : <>
                                <Bar headLine={"Trading System"} />
                                <SearchBar />
                                <Divider sx={{ marginTop: 1 }} />
                                <ShopsBar />
                                <Divider />
                                {/* <Categories /> */}
                                <Categories2 />
                                {products ? products.map((product: Product) => {
                                    <ProductCard item={product} key={product.id} canEdit={false} canDelete={false} />
                                }) : null}
                                <Outlet />
                            </>
            }
            <Outlet />
        </>


    )
};

export default DashboardPage;