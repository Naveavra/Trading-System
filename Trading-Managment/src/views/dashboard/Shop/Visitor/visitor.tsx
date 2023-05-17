import { Card, CardContent, Typography } from "@mui/material";
import { Box } from "@mui/system";
import { Outlet } from "react-router";
import Bar2 from "../../../../components/Bars/Navbar/NavBar2";
import ProductDisplay from "../../../../components/Product/Product";
import ProductCard from "../../../../components/ProductCard/Card";
import { useAppDispatch, useAppSelector } from "../../../../redux/store";
import { useEffect } from "react";
import { getProducts } from "../../../../reducers/productsSlice";
import { getStoresInfo } from "../../../../reducers/storesSlice";
import axios from "axios";
import { getClientData } from "../../../../reducers/authSlice";


const Visitor: React.FC = () => {
    const dispatch = useAppDispatch();

    const userId = useAppSelector((state) => state.auth.userId);
    const token = useAppSelector((state) => state.auth.token);
    const store = useAppSelector((state) => state.store.storeState.wahtchedStoreInfo);
    const products = useAppSelector((state) => state.product.responseData);

    const ourProducts = products?.filter((product) => product.storeId === store.id);
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds
    const PING_INTERVAL2 = 5000;
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
    const getC = () => {
        if (token) {
            dispatch(getClientData({ userId: userId, token: token }));
        }
    }
    useEffect(() => {
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        const pingInterval2 = setInterval(getC, PING_INTERVAL2);
        dispatch(getStoresInfo());
        dispatch(getProducts());
        if (token) {
            dispatch(getClientData({ userId: userId, token: token }));
        }
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
            clearInterval(pingInterval2)
        };
    }, [dispatch]);
    return (
        <>
            <Bar2 headLine={`wellcome to the store`} />
            <Box>
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
                                decreaption about the store
                            </Typography >
                        </Box>

                    </CardContent>
                </Card>
            </Box >
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
export default Visitor;
