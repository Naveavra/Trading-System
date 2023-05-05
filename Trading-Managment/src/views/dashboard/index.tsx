import * as React from 'react';
//import DashboardFrame from '../../components';
// import { Navigate, Outlet } from 'react-router-dom';
// // import { useAppSelector } from '../../redux/store';
//import '../../logos/cartLogo.css'
import { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import DashboardFrame from '../../components/Frame';
import AlertDialog from '../../components/Dialog/AlertDialog';
import { clearAuthError } from '../../reducers/authSlice';
const DashboardPage: React.FC = () => {
    const dispatch = useAppDispatch();
    const isLoadingShops = false//useAppSelector((state) => !!state.shops.isLoading);
    const isLoadingProducts = false //useAppSelector((state) => !!state.products.isLoading);
    const userId = useAppSelector((state) => state.auth.userId);
    const error = useAppSelector((state) => state.auth.error);
    // const products = useAppSelector((state) => state.products.responseBody);
    // const categories = useAppSelector((state) => state.categories.responseBody);
    // Define variables for the cart elements
    console.log('userId', userId);
    useEffect(() => {
        // dispatch(getShops({}));
        // dispatch(getProducts({ category: 'all' }));
    }, [dispatch])
    return (isLoadingShops || isLoadingProducts ?
        <>
            <div id='cart'>
                <div className="preloader">
                    <svg className="cart" role="img" aria-label="Shopping cart line animation" viewBox="0 0 128 128" width="128px"
                        height="128px" xmlns="http://www.w3.org/2000/svg">
                        <g fill="none" stroke-linecap="round" stroke-linejoin="round" stroke-width="8">
                            <g className="cart__track" stroke="hsla(0,10%,10%,0.1)">
                                <polyline points="4,4 21,4 26,22 124,22 112,64 35,64 39,80 106,80" />
                                <circle cx="43" cy="111" r="13" />
                                <circle cx="102" cy="111" r="13" />
                            </g>
                            <g className="cart__lines" stroke="currentColor">
                                <polyline className="cart__top" points="4,4 21,4 26,22 124,22 112,64 35,64 39,80 106,80"
                                    stroke-dasharray="338 338" stroke-dashoffset="-338" />
                                <g className="cart__wheel1" transform="rotate(-90,43,111)">
                                    <circle className="cart__wheel-stroke" cx="43" cy="111" r="13" stroke-dasharray="81.68 81.68"
                                        stroke-dashoffset="81.68" />
                                </g>
                                <g className="cart__wheel2" transform="rotate(90,102,111)">
                                    <circle className="cart__wheel-stroke" cx="102" cy="111" r="13" stroke-dasharray="81.68 81.68"
                                        stroke-dashoffset="81.68" />
                                </g>
                            </g>
                        </g>
                    </svg>
                    <div className="preloader__text">
                        <p className="preloader__msg">Bringing you the goods…</p>
                    </div>
                </div>
            </div>
        </> : !!error ? <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()) }} text={error} sevirity={'error'} />
            : <DashboardFrame />

    )
};

export default DashboardPage;