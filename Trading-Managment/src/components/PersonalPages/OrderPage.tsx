import axios from "axios";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { useEffect } from "react";
import { Box, Button, Card, CardActions, CardContent, CardMedia, IconButton, Typography } from "@mui/material";
import Bar3 from "../Bars/Navbar/NavBar3";
import RateReviewIcon from '@mui/icons-material/RateReview';
import { useNavigate } from "react-router-dom";
import { clearAuthError } from "../../reducers/authSlice";
import { clearStoreError, clearStoresResponse } from "../../reducers/storesSlice";
import ErrorAlert from "../Alerts/error";
import SuccessAlert from "../Alerts/success";


const OrderPage = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const order = useAppSelector((state) => state.auth.whatchedOrder);
    const userId = useAppSelector((state) => state.auth.userId);
    const stores = useAppSelector((state) => state.store.storeInfoResponseData);
    const error = useAppSelector((state) => state.store.storeState.error);
    const message = useAppSelector((state) => state.store.storeState.responseData);


    return (
        <>
            <Bar3 headLine={"this is youre order"} />

            <Box sx={{ ml: 2 }}>
                <h1>Order Details</h1>
                <p>Order ID: {order.orderId}</p>
                <p>User ID: {order.userId}</p>
                <p>Total Price: {order.totalPrice}</p>
                <Button variant="contained" onClick={() => navigate(`reviewStore`)}>Review store in order</Button>
            </Box >
            {message ? <SuccessAlert message={message} onClose={() => { dispatch(clearStoresResponse({})) }} /> : null}
            {error ? <ErrorAlert message={error} onClose={() => { dispatch(clearStoreError({})) }} /> : null}
            <h2>Products in Order</h2>
            <Box sx={{ ml: 2 }} display="flex" alignItems="center">
                {order.productsInStores?.map((product) => (
                    <Card sx={{ ml: 2, mr: 2, width: 250 }}>
                        <CardMedia
                            sx={{ height: 140 }}
                            image={product.img}
                            title={product.name}
                        />
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div">
                                {product.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                <p>Price: {product.price}</p>
                                <p>Store Name: {stores.find(store => store.storeId === product.storeId)?.name}</p>
                                <p>Quantity: {product.quantity}</p>
                                <p>Description: {product.description}</p>
                                <p>Rating: {product.rating}</p>
                            </Typography>
                        </CardContent>
                        <CardActions>
                            <IconButton sx={{ marginLeft: 'auto' }} onClick={() => navigate(`reviewProduct/${product.storeId}/${product.productId}`)}>
                                <RateReviewIcon />
                            </IconButton>
                        </CardActions>
                    </Card>
                ))}
            </Box>


        </>
    );
}
export default OrderPage;