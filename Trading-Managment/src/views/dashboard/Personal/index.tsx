import { useMemo, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../../redux/store";
import { Card, CardContent, Typography, CardActions, Button, Divider } from "@mui/material";
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import { useNavigate } from "react-router-dom";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { GridColDef } from "@mui/x-data-grid";

const Personal = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const email = useAppSelector((state) => state.auth.userName);
    const age = useAppSelector((state) => state.auth.age);
    const name = email.split('@')[0];
    const birthday = useAppSelector((state) => state.auth.birthday);

    const orders = useAppSelector((state) => state.auth.purchaseHistory);
    return (
        <>
            <Bar3 headLine={"this is your personal data"} />
            <Card sx={{ minWidth: 275, width: '30%', mt: 5, ml: 3 }}>
                <CardContent>
                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                        name : {name}
                    </Typography>
                    <Typography variant="h5" component="div">
                        age : {age}
                    </Typography>
                    <Typography sx={{ mb: 1.5 }} color="text.secondary">
                        email : {email}
                    </Typography>
                    <Typography variant="body2">
                        birthday : {birthday}
                    </Typography>
                </CardContent>
                <CardActions>
                    <IconButton onClick={() => navigate("editMyProfile")} >
                        <EditIcon />
                    </IconButton>
                </CardActions>
            </Card>
            <Divider />
            <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                your purchase history
            </Typography>
            {orders.map((order, index) => {
                return (
                    <Card sx={{ minWidth: 275, width: '30%', mt: 5, ml: 3 }} key={index} onClick={() => navigate(`order/${order.orderId}`)}>
                        <CardContent>
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

        </>
    );
};
export default Personal;