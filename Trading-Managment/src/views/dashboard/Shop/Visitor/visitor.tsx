import { Card, CardActions, CardContent, IconButton, Slider, SliderThumb, Typography, styled } from "@mui/material";
import { Box } from "@mui/system";
import { Outlet } from "react-router";
import ProductCard from "../../../../components/ProductInStore/Card";
import { useAppDispatch, useAppSelector } from "../../../../redux/store";
import { useEffect } from "react";
import { getProducts } from "../../../../reducers/productsSlice";
import { clearStoreError, clearStoresResponse, getStoresInfo } from "../../../../reducers/storesSlice";
import axios from "axios";
import Bar3 from "../../../../components/Bars/Navbar/NavBar3";
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import { useNavigate } from "react-router-dom";
import ErrorAlert from "../../../../components/Alerts/error";
import SuccessAlert from "../../../../components/Alerts/success";
import { clearBidMsg, clearBidError } from "../../../../reducers/bidSlice";
import { clearCartError, clearCartMessage } from "../../../../reducers/cartSlice";
import React from "react";

const Visitor: React.FC = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const userId = useAppSelector((state) => state.auth.userId);
    const store = useAppSelector((state) => state.store.storeState.wahtchedStoreInfo);
    const products = useAppSelector((state) => state.product.responseData);

    const ourProducts = products?.filter((product) => product.storeId === store.storeId);
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds

    const storeError = useAppSelector((state) => state.store.storeState.error);
    const storeMessage = useAppSelector((state) => state.store.storeState.responseData);

    const bidMessage = useAppSelector((state) => state.bid.message);
    const bidError = useAppSelector((state) => state.bid.error);

    const cartMessage = useAppSelector((state) => state.cart.basketState.responseData);
    const cartError = useAppSelector((state) => state.cart.basketState.error);

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
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };
    }, [dispatch]);
    return (
        <>
            <Bar3 headLine={`wellcome to the store`} />
            {storeMessage ? <SuccessAlert message={storeMessage} onClose={() => { dispatch(clearStoresResponse({})) }} /> : null}
            {storeError ? <ErrorAlert message={storeError} onClose={() => { dispatch(clearStoreError({})) }} /> : null}
            {bidMessage ? <SuccessAlert message={bidMessage} onClose={() => { dispatch(clearBidMsg()) }} /> : null}
            {bidError ? <ErrorAlert message={bidError} onClose={() => { dispatch(clearBidError()) }} /> : null}
            {cartMessage ? <SuccessAlert message={cartMessage} onClose={() => { dispatch(clearCartMessage()) }} /> : null}
            {cartError ? <ErrorAlert message={cartError} onClose={() => { dispatch(clearCartError()) }} /> : null}
            <Card sx={{ minWidth: 275 }}>
                <Box display={"flex"}>
                    <>
                        <Card sx={{ minWidth: 275 }}>
                            <CardContent>
                                <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                                    about us
                                </Typography >
                                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>

                                </Typography>
                                <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                                    <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 73, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                        {store.description}
                                    </Typography >
                                </Box>
                                <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                                    <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 73, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                        store discounts
                                    </Typography >
                                    {store.discounts?.map((discount) => {
                                        return (
                                            <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 73, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                                {discount}
                                            </Typography >
                                        )
                                    })}
                                </Box>
                                <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                                    <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 73, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                        store shopping rules
                                    </Typography >
                                    {store.shoppingRules?.map((shopRule) => {
                                        return (
                                            <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 73, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                                {shopRule}
                                            </Typography >
                                        )
                                    })}
                                </Box>
                            </CardContent>
                            <CardActions>
                                <IconButton aria-label="delete" color="primary" sx={{ ml: 1, mt: 1 }} onClick={() => navigate('SendQuestion')}>
                                    ask us anything
                                    <QuestionMarkIcon />
                                </IconButton>
                            </CardActions>
                        </Card>
                        <Box sx={{ marginTop: 1, ml: '15%', mt: 3, width: '20%' }}  >
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
                    </>
                </Box >
            </Card>
            <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                {!!ourProducts && ourProducts.map((product) => {
                    return (
                        <ProductCard item={product} canDelete={false} canEdit={false} key={product.productId} />
                    );
                })
                }
            </Box>
            <Outlet />
        </>
    );
}
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

export default Visitor;
