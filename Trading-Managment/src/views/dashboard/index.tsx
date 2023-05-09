import * as React from 'react';

import { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import DashboardFrame from '../../components/Frame';
import AlertDialog from '../../components/Dialog/AlertDialog';
import { clearAuthError } from '../../reducers/authSlice';
import CartLogo from '../../components/Loaders/cartLoader';
import { clearStoresError } from '../../reducers/storesSlice';
import axios from 'axios';
import { Outlet } from 'react-router-dom';

const DashboardPage: React.FC = () => {
    const dispatch = useAppDispatch();
    const isLoadingShops = false//useAppSelector((state) => !!state.shops.isLoading);
    const isLoadingProducts = false //useAppSelector((state) => !!state.products.isLoading);
    const userId = useAppSelector((state) => state.auth.userId);
    const error = useAppSelector((state) => state.auth.error);
    const shopError = false//useAppSelector((state) => state.shops.error);
    const productError = false//useAppSelector((state) => state.products.error);

    return (
        <>
            {isLoadingShops || isLoadingProducts ?
                <CartLogo />
                : !!error ? <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()) }} text={error} sevirity={'error'} />
                    : !!shopError ? <AlertDialog open={!!shopError} onClose={() => { dispatch(clearStoresError()) }} text={shopError} sevirity={'error'} />
                        : !!productError ? <AlertDialog open={!!productError} onClose={() => { dispatch(clearProductsError()) }} text={productError} sevirity={'error'} />
                            : <DashboardFrame />
            }
            <Outlet />
        </>


    )
};

export default DashboardPage;