import { useAppDispatch, useAppSelector } from "../../../redux/store";
import { Card, CardContent, Typography, CardActions, Divider, Box, Alert, Button } from "@mui/material";
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import { Outlet, useNavigate } from "react-router-dom";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { clearAuthError, clearAuthMsg, getClientData, setWatchedOrder } from "../../../reducers/authSlice";
import PasswordIcon from '@mui/icons-material/Password';
import SuccessAlert from "../../../components/Alerts/success";
import ErrorAlert from "../../../components/Alerts/error";
import RateReviewIcon from '@mui/icons-material/RateReview';
import InfoIcon from '@mui/icons-material/Info';
import { status } from '../../../types/systemTypes/Bid'
import { clearBidMsg, clearBidError, answerOnCounter } from "../../../reducers/bidSlice";
import { useEffect } from "react";
const Personal = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const userId = useAppSelector((state) => state.auth.userId);
    const email = useAppSelector((state) => state.auth.userName);
    const age = useAppSelector((state) => state.auth.age);
    const name = email.split('@')[0];
    const birthday = useAppSelector((state) => state.auth.birthday);

    const orders = useAppSelector((state) => state.auth.purchaseHistory);
    const bids = useAppSelector((state) => state.auth.bids);
    console.log(bids);

    const userMessage = useAppSelector((state) => state.auth.message);
    const userError = useAppSelector((state) => state.auth.error);

    const bidMessage = useAppSelector((state) => state.bid.message);
    const bidError = useAppSelector((state) => state.bid.error);


    const handleClickOrder = (orderId: number) => {
        dispatch(setWatchedOrder(orderId));
        navigate(`order/${orderId}`)
    };
    useEffect(() => {
        dispatch(getClientData({ userId: userId }))
    }, []);

    return (
        <>
            <Bar3 headLine={"this is your personal data"} />
            {userMessage ? <SuccessAlert message={userMessage} onClose={() => { dispatch(clearAuthMsg()) }} /> : null}
            {userError ? <ErrorAlert message={userError} onClose={() => { dispatch(clearAuthError()) }} /> : null}
            {bidMessage ? <SuccessAlert message={bidMessage} onClose={() => { dispatch(clearBidMsg()) }} /> : null}
            {bidError ? <ErrorAlert message={bidError} onClose={() => { dispatch(clearBidError()) }} /> : null}
            <Box sx={{ width: '100%', display: 'flex' }}>
                <Typography sx={{ fontSize: 25, mt: 3, ml: '10%' }} gutterBottom>
                    your personal data
                </Typography>
            </Box>
            <Box sx={{ width: '100%', display: 'flex', mb: 2 }}>
                <Card sx={{ minWidth: 275, width: '30%', mt: 5, ml: 3 }}>
                    <CardContent sx={{ padding: 2 }}>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5" gutterBottom>
                            name : {name}
                        </Typography>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5" component="div">
                            age : {age}
                        </Typography>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                            email : {email}
                        </Typography>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                            birthday : {birthday}
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <IconButton onClick={() => navigate("editMyProfile")} sx={{ marginLeft: 'auto' }}>
                            <EditIcon />
                        </IconButton>
                        <IconButton onClick={() => navigate("changePassword")} sx={{ marginLeft: 'auto' }}>
                            <PasswordIcon />
                        </IconButton>
                    </CardActions>
                </Card>
            </Box >
            <Divider />
            <Typography sx={{ fontSize: 25, mt: 3, display: 'flex', justifyContent: 'center' }} gutterBottom>
                your purchase history
            </Typography>
            <Box sx={{ display: "flex", width: '100%', mb: 2 }}>
                {
                    orders?.map((order, index) => {
                        return (
                            <Card sx={{ width: 200, mt: 5, ml: 3 }} key={index}>
                                <CardContent>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        orderId:  {order.orderId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        price:  {order.totalPrice}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        quntity:  {order.productsInStores?.length}
                                    </Typography>
                                </CardContent>
                                <CardActions>
                                    <IconButton onClick={() => handleClickOrder(order.orderId)}>
                                        <InfoIcon />
                                    </IconButton>
                                    <IconButton onClick={() => navigate(`${order.orderId}/sendComplaint`)}>
                                        <RateReviewIcon />
                                    </IconButton>
                                </CardActions>
                            </Card>
                        )
                    })
                }
            </Box>
            <Typography sx={{ fontSize: 25, mt: 3, display: 'flex', justifyContent: 'center' }} gutterBottom>
                your bids
            </Typography>
            <Box sx={{ display: "flex", width: '100%', mb: 2 }}>
                {
                    bids?.map((bid, index) => {
                        return (
                            <Card sx={{ width: 200, mt: 5, ml: 3 }} key={index}>
                                <CardContent>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        bidId:  {bid.bidId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        storeId:  {bid.storeId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        product: {bid.product.name}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        current offer: {String(bid.offer)}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        amount: {bid.quantity}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        product price: {bid.product.price}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        time: {bid.time}
                                    </Typography>
                                    {
                                        bid.state === status.Completed ?
                                        <>
                                        <Alert severity="success">Completed</Alert>
                                        </> :
                                        bid.state === status.Approved ?
                                            <>
                                                <Alert severity="info">approved</Alert>
                                                <Button onClick={() => navigate(`${bid.bidId}/makePurchase`)}>enter details and make purchase</Button>
                                            </> :
                                            bid.state === status.Pending ?
                                                <Alert severity="info">pending</Alert> :
                                                bid.state === status.Counter ?
                                                    <>
                                                        <Alert severity="info">counter</Alert>
                                                        <Button onClick={() => {
                                                            dispatch(answerOnCounter({ bidId: bid.bidId, storeId: bid.storeId }));
                                                            dispatch(getClientData({ userId: userId }));

                                                        }}>approved store offer</Button>
                                                        <Button onClick={() => {
                                                            navigate(`${bid.storeId}/${bid.product.productId}/${bid.bidId}/EditBid`);
                                                        }
                                                        }>counter offer</Button>
                                                    </>
                                                    :
                                                    <Alert severity="error">reject</Alert>
                                    }
                                </CardContent>
                                <CardActions>
                                </CardActions>
                            </Card>
                        )
                    })
                }
            </Box>
            <Outlet />
        </>
    );
};
export default Personal;