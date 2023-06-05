import axios from "axios";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { useEffect } from "react";
import { Box, Button, Card, CardActions, CardContent, CardMedia, Typography } from "@mui/material";
import Bar3 from "../Bars/Navbar/NavBar3";

const OrderPage = () => {
    const dispath = useAppDispatch();
    const order = useAppSelector((state) => state.auth.whatchedOrder);
    const userId = useAppSelector((state) => state.auth.userId);

    const PING_INTERVAL = 10000; // 10 seconds in milliseconds

    const sendPing = () => {
        if (userId != 0) {
            axios.post('http://localhost:4567/api/auth/ping', { userId: userId })
                .then(() => {
                    // Do something with the response if necessary
                })
                .catch(() => {
                    // Handle the error if necessary
                });
            // dispatch(ping(userId));
        }
    }
    useEffect(() => {
        const pingInterval = setInterval(sendPing, PING_INTERVAL);

        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };

    }, [dispath, userId]);
    return (
        <>
            <Bar3 headLine={"this is youre order"} />
            <div>
                <h1>Order Details</h1>
                <p>Order ID: {order.orderId}</p>
                <p>User ID: {order.userId}</p>
                <p>Total Price: {order.totalPrice}</p>

                <h2>Products in Order</h2>
                <Box display='flex'>
                    {order.products?.map((product) => (
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
                                    <p>Quantity: {product.quantity}</p>
                                    <p>Description: {product.description}</p>
                                    <p>Rating: {product.rating}</p>
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <Button size="small">Share</Button>
                            </CardActions>
                        </Card>
                    ))}
                </Box>
            </div>

        </>
    );
}
export default OrderPage;