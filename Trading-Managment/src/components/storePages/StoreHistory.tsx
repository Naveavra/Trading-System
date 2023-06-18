import { Box, Card, CardContent, Typography, Alert, CardActions } from "@mui/material";
import { useAppSelector } from "../../redux/store";
import Bar2 from "../Bars/Navbar/NavBar2";

const StoreHistory = () => {
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const orders = store.storeOrders;
    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    console.log(orders);

    return (
        <>
            <Bar2 headLine={`hello ${privateName} , wellcome to `} />
            <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                more specific order details
            </Typography >
            <Box sx={{ display: "flex", width: '100%', mb: 2 }}>
                {
                    orders?.map((order, index) => {
                        return (
                            <Card sx={{ width: 350, mt: 5, ml: 3 }} key={index}>
                                <CardContent>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        order id:  {order.orderId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        userId:  {order.userId}
                                    </Typography>
                                    {order.productsInStores?.map((product, index) => {
                                        return (
                                            <>
                                                <Box sx={{ display: "flex", width: '90%', mb: 2 }}>
                                                    <Typography sx={{ fontSize: 14, mr: 2 }} color="text.secondary" gutterBottom>
                                                        product: {product.name}
                                                    </Typography>
                                                    <Typography sx={{ fontSize: 14, mr: 2, justifyContent: 'center' }} color="text.secondary" gutterBottom>
                                                        amount: {product.quantity}
                                                    </Typography>
                                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                                        price: {product.price}
                                                    </Typography>
                                                </Box>
                                            </>
                                        )
                                    })}

                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        total price: {order.totalPrice}
                                    </Typography>

                                </CardContent>
                                <CardActions>
                                    {/* <IconButton onClick={() => handleClickOrder(order.orderId)}>
                                        <InfoIcon />
                                    </IconButton>
                                    <IconButton onClick={() => navigate(`${order.orderId}/sendComplaint`)}>
                                        <RateReviewIcon />
                                    </IconButton> */}
                                </CardActions>
                            </Card>
                        )
                    })
                }
            </Box>
        </>
    )
}
export default StoreHistory;