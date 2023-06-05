import { Card, CardActions, CardContent, IconButton, Typography } from "@mui/material";
import { Box } from "@mui/system";
import { Outlet } from "react-router";
import ProductCard from "../../../../components/ProductInStore/Card";
import { useAppDispatch, useAppSelector } from "../../../../redux/store";
import { useEffect } from "react";
import { getProducts } from "../../../../reducers/productsSlice";
import { getStoresInfo } from "../../../../reducers/storesSlice";
import axios from "axios";
import Bar3 from "../../../../components/Bars/Navbar/NavBar3";
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import { useNavigate } from "react-router-dom";

const Visitor: React.FC = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const userId = useAppSelector((state) => state.auth.userId);
    const store = useAppSelector((state) => state.store.storeState.wahtchedStoreInfo);
    const products = useAppSelector((state) => state.product.responseData);

    const ourProducts = products?.filter((product) => product.storeId === store.storeId);
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
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };
    }, [dispatch]);
    return (
        <>
            <Bar3 headLine={`wellcome to the store`} />
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
                            </Typography >
                        </Box>

                    </CardContent>
                    <CardActions>
                        <IconButton aria-label="delete" color="primary" sx={{ ml: 1, mt: 1 }} onClick={() => navigate('SendQuestion')}>
                            ask us anything
                            <QuestionMarkIcon />
                        </IconButton>
                    </CardActions>
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
