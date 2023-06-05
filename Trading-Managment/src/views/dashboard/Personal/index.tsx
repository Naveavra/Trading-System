import { useAppDispatch, useAppSelector } from "../../../redux/store";
import { Card, CardContent, Typography, CardActions, Divider, Box } from "@mui/material";
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import { Outlet, useNavigate } from "react-router-dom";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { clearAuthError, clearAuthMsg, setWatchedOrder } from "../../../reducers/authSlice";
import PasswordIcon from '@mui/icons-material/Password';
import SuccessAlert from "../../../components/Alerts/success";
import ErrorAlert from "../../../components/Alerts/error";

const Personal = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const email = useAppSelector((state) => state.auth.userName);
    const age = useAppSelector((state) => state.auth.age);
    const name = email.split('@')[0];
    const birthday = useAppSelector((state) => state.auth.birthday);
    const userMessage = useAppSelector((state) => state.auth.message);
    const userError = useAppSelector((state) => state.auth.error);
    const orders = useAppSelector((state) => state.auth.purchaseHistory);
    const stores = useAppSelector((state) => state.store.storeInfoResponseData);

    const handleClickOrder = (orderId: number) => {
        dispatch(setWatchedOrder(orderId));
        navigate(`order/${orderId}`)
    };

    return (
        <>
            <Bar3 headLine={"this is your personal data"} />
            {userMessage ? <SuccessAlert message={userMessage} onClose={() => { dispatch(clearAuthMsg()) }} /> : null}
            {userError ? <ErrorAlert message={userError} onClose={() => { dispatch(clearAuthError()) }} /> : null}
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
            <Typography sx={{ fontSize: 25, mt: 3, ml: '40%' }} gutterBottom>
                your purchase history
            </Typography>
            {
                orders?.map((order, index) => {
                    return (
                        <Card sx={{ width: 200, mt: 5, ml: 3 }} key={index} onClick={() => handleClickOrder(order.orderId)}>
                            <CardContent>
                                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                    orderId:  {order.orderId}
                                </Typography>
                                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                    price:  {order.totalPrice}
                                </Typography>
                                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                    quntity:  {order.products.length}
                                </Typography>
                            </CardContent>
                        </Card>
                    )
                })
            }
            <Outlet />
        </>
    );
};
export default Personal;