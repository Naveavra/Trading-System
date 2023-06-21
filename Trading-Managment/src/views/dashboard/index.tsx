import * as React from 'react';

import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import AlertDialog from '../../components/Dialog/AlertDialog';
import { clearAuthError, getClientData, getNotifications, resetAuth } from '../../reducers/authSlice';
import CartLogo from '../../components/Loaders/cartLoader';
import { clearStoresError, clearStoresResponse, getStore, getStoresInfo } from '../../reducers/storesSlice';
import axios from 'axios';
import { Outlet, useNavigate } from 'react-router-dom';
import { clearProductsError, getProducts } from '../../reducers/productsSlice';
import { Box, Divider, Slider, SliderThumb, Typography, styled } from '@mui/material';
import Bar from '../../components/Bars/Navbar/Navbar';
import { SearchBar } from '../../components/Bars/SearchBar/SearchBar';
import ShopsBar from '../../components/Bars/ShopBar/ShopBar';
import Categories2 from '../../components/Categories/category2';
import Products from '../../components/Product/Products';
import { getCart } from '../../reducers/cartSlice';
import SuccessAlert from '../../components/Alerts/success';
import { getComplaints, getLogger, getMarketStatus, removeUser } from '../../reducers/adminSlice';

const DashboardPage: React.FC = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [text, setText] = useState('');
    const [left, setLeft] = useState(false);
    const isLoadingShops = useAppSelector((state) => !!state.store.isLoading);
    const isLoadingProducts = useAppSelector((state) => !!state.product.isLoading);

    const userId = useAppSelector((state) => state.auth.userId);
    const token = useAppSelector((state) => state.auth.token) ?? "";
    const userName = useAppSelector((state) => state.auth.userName);
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const error = useAppSelector((state) => state.auth.error);
    const shopError = useAppSelector((state) => state.store.error);
    const productError = useAppSelector((state) => state.product.error);



    const [price, setPrice] = React.useState<number[]>([0, 10000]);
    const [productRating, setProductRating] = React.useState<number[]>([0, 5]);
    const [storeRating, setStoreRating] = React.useState<number[]>([0, 5]);

    const handleChangePrice = (event: Event, newValue: number | number[]) => {
        setPrice(newValue as number[]);
    };
    const handleChangeProductRating = (event: Event, newValue: number | number[]) => {
        setProductRating(newValue as number[]);
    };
    const handleChangeStoreRating = (event: Event, newValue: number | number[]) => {
        setStoreRating(newValue as number[]);
    };
    // const cart = useAppSelector((state) => state.cart.responseData);
    // const numProductsIncart = cart?.reduce((acc, item) => acc + item.quantity, 0) ?? 0;

    //success alerts
    const openStoreAlert = useAppSelector((state) => state.store.storeState.responseData);
    const handleAdmin = () => {
        dispatch(getLogger(userId));
        dispatch(getComplaints(userId));
        dispatch(getMarketStatus(userId));
    }

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
    interface NumberToVoidFunctionMap {
        [key: number]: () => void;
    }

    const hashMap: NumberToVoidFunctionMap = {
        0: () => {
            dispatch(getClientData({ userId: userId }));
            fetchNotification();
        },
        1: () => {
            dispatch(getStore({ userId: userId, storeId: storeId }));
            fetchNotification();
        },
        2: () => {
            handleAdmin();
            fetchNotification();
        },
        3: () => {
            dispatch(getComplaints(userId));
            fetchNotification();
        },
        4: () => {
            setLeft(true);
            dispatch(removeUser(userName));
            dispatch(resetAuth());
            navigate('/auth/login');
        },
        5: () => {
            dispatch(getClientData({ userId: userId }));
            dispatch(getStore({ userId: userId, storeId: storeId }));
            fetchNotification();
        },
        6: () => {
            dispatch(getClientData({ userId: userId }));
            dispatch(getComplaints(userId));
            fetchNotification();
        },
        7: () => {
            dispatch(getStore({ userId: userId, storeId: storeId }));
            dispatch(getComplaints(userId));
            fetchNotification();
        },
    };
    const fetchNotification = async () => {
        try {
            if (token != "" && userName != 'guest' && !left) {
                const response = await dispatch(getNotifications({ userId: userId, token: token }));
                if (response.payload != null) {
                    hashMap[response.payload?.opcode ?? 0]();
                }
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
    }, [userId, dispatch, token, userName, storeId])
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
                                <Box sx={{ marginTop: 1, width: '100%' }} display={'flex'} >
                                    <Box sx={{ marginTop: 1, width: '50%' }} display={'flex'} >
                                        <Categories2 />
                                    </Box>
                                    <Box sx={{ marginTop: 1, ml: '25%', width: '20%' }}  >
                                        <Typography id="input-slider" gutterBottom sx={{ mb: 2 }}>
                                            Price range
                                        </Typography>
                                        <AirbnbSlider
                                            value={price}
                                            onChange={handleChangePrice}
                                            slots={{ thumb: AirbnbThumbComponent }}
                                            getAriaLabel={(index) => (index === 0 ? 'Minimum price' : 'Maximum price')}
                                            defaultValue={[0, 4000]}
                                            max={5000}
                                            valueLabelDisplay="on"
                                        />
                                        <Typography gutterBottom sx={{ mb: 2 }}>product rating</Typography>
                                        <Slider
                                            getAriaLabel={() => 'product rating'}
                                            value={productRating}
                                            onChange={handleChangeProductRating}
                                            valueLabelDisplay="auto"
                                            marks={marks}
                                            max={5}
                                            min={0}
                                            step={1}
                                        />
                                        <Typography gutterBottom sx={{ mb: 2 }}>store rating</Typography>
                                        <Slider
                                            getAriaLabel={() => 'store rating'}
                                            value={storeRating}
                                            onChange={handleChangeStoreRating}
                                            valueLabelDisplay="auto"
                                            marks={marks}
                                            max={5}
                                            min={0}
                                            step={1}
                                        />
                                    </Box>
                                </Box>
                                <Divider />
                                <Products text={text} price={price} productRating={productRating} storeRating={storeRating} />
                                <Outlet />
                            </>
            }

            <Outlet />

        </>


    )
};
const AirbnbSlider = styled(Slider)(({ theme }) => ({
    color: '#3a8589',
    height: 3,
    padding: '13px 0',
    '& .MuiSlider-thumb': {
        height: 27,
        width: 27,
        backgroundColor: '#fff',
        border: '1px solid currentColor',
        '&:hover': {
            boxShadow: '0 0 0 8px rgba(58, 133, 137, 0.16)',
        },
        '& .airbnb-bar': {
            height: 9,
            width: 1,
            backgroundColor: 'currentColor',
            marginLeft: 1,
            marginRight: 1,
        },
    },
    '& .MuiSlider-track': {
        height: 3,
    },
    '& .MuiSlider-rail': {
        color: theme.palette.mode === 'dark' ? '#bfbfbf' : '#d8d8d8',
        opacity: theme.palette.mode === 'dark' ? undefined : 1,
        height: 3,
    },
}));

interface AirbnbThumbComponentProps extends React.HTMLAttributes<unknown> { }

function AirbnbThumbComponent(props: AirbnbThumbComponentProps) {
    const { children, ...other } = props;
    return (
        <SliderThumb {...other}>
            {children}
            <span className="airbnb-bar" />
            <span className="airbnb-bar" />
            <span className="airbnb-bar" />
        </SliderThumb>
    );
}
const iOSBoxShadow =
    '0 3px 1px rgba(0,0,0,0.1),0 4px 8px rgba(0,0,0,0.13),0 0 0 1px rgba(0,0,0,0.02)';

const marks = [
    {
        value: 0,
    },
    {
        value: 1,
    },
    {
        value: 2,
    },
    {
        value: 3,
    },
    {
        value: 4,
    },
    {
        value: 5,
    },
];

const IOSSlider = styled(Slider)(({ theme }) => ({
    color: theme.palette.mode === 'dark' ? '#3880ff' : '#3880ff',
    height: 2,
    padding: '15px 0',
    '& .MuiSlider-thumb': {
        height: 28,
        width: 28,
        backgroundColor: '#fff',
        boxShadow: iOSBoxShadow,
        '&:focus, &:hover, &.Mui-active': {
            boxShadow:
                '0 3px 1px rgba(0,0,0,0.1),0 4px 8px rgba(0,0,0,0.3),0 0 0 1px rgba(0,0,0,0.02)',
            // Reset on touch devices, it doesn't add specificity
            '@media (hover: none)': {
                boxShadow: iOSBoxShadow,
            },
        },
    },
    '& .MuiSlider-valueLabel': {
        fontSize: 12,
        fontWeight: 'normal',
        top: -6,
        backgroundColor: 'unset',
        color: theme.palette.text.primary,
        '&:before': {
            display: 'none',
        },
        '& *': {
            background: 'transparent',
            color: theme.palette.mode === 'dark' ? '#fff' : '#000',
        },
    },
    '& .MuiSlider-track': {
        border: 'none',
    },
    '& .MuiSlider-rail': {
        opacity: 0.5,
        backgroundColor: '#bfbfbf',
    },
    '& .MuiSlider-mark': {
        backgroundColor: '#bfbfbf',
        height: 8,
        width: 1,
        '&.MuiSlider-markActive': {
            opacity: 1,
            backgroundColor: 'currentColor',
        },
    },
}));


export default DashboardPage;
