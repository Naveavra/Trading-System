import { useAppDispatch, useAppSelector } from "../../../redux/store";
import { Card, CardContent, Typography, CardActions, Divider, Box, Grid, TextField } from "@mui/material";
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import { Outlet, useNavigate } from "react-router-dom";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { LoadingButton } from "@mui/lab";
import { useForm } from "react-hook-form";
import { sendMsgFormValues } from "../../../types/formsTypes";
import { sendMessage } from "../../../reducers/authSlice";
import { useState } from "react";

const Personal = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<sendMsgFormValues>();

    const userId = useAppSelector((state) => state.auth.userId);
    const email = useAppSelector((state) => state.auth.userName);
    const age = useAppSelector((state) => state.auth.age);
    const name = email.split('@')[0];
    const birthday = useAppSelector((state) => state.auth.birthday);
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const [emailError, setEmailError] = useState("");

    const orders = useAppSelector((state) => state.auth.purchaseHistory);
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        dispatch(sendMessage(form.getValues()));
    }
    const validateEmail = (email: string): void => {
        const emailRegex: RegExp = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if (!emailRegex.test(email)) {
            form.setError("userName", { type: 'custom', message: 'email isnt valide' }, { shouldFocus: true })
            setEmailError("email isnt valide")
        }
        else {
            setEmailError("");
            form.clearErrors("userName");
        }
        // return emailRegex.test(email);
    }
    return (
        <>
            <Bar3 headLine={"this is your personal data"} />
            <Box sx={{ width: '100%', display: 'flex' }}>
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
                    </CardActions>
                </Card>
                <Grid
                    spacing={2}
                    container
                    component="form"
                    onSubmit={handleOnSubmit}
                    sx={{ width: '50%', ml: 10, mt: 5 }}
                >
                    <Grid item xs={12}>
                        <TextField
                            name="email"
                            type="text"
                            fullWidth
                            label="name"
                            sx={{ mt: 1, mb: 1 }}
                            inputProps={{
                                ...form.register('userName', {
                                    required: {
                                        value: true,
                                        message: "name is required"
                                    }
                                })
                            }}
                            error={!!form.formState.errors['userName'] ?? false}
                            helperText={form.formState.errors['userName']?.message ?? undefined}
                            onChange={(e) => {
                                validateEmail(e.target.value)
                            }}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            name="message"
                            type="text"
                            fullWidth
                            label="message"
                            sx={{ mt: 1, mb: 1 }}
                            inputProps={{
                                ...form.register('message', {
                                    required: {
                                        value: true,
                                        message: "msg is mendatory"
                                    }
                                })
                            }}
                            error={!!form.formState.errors['message'] ?? false}
                            helperText={form.formState.errors['message']?.message ?? undefined}
                        />
                    </Grid>

                    <Grid item xs={12}>
                        <LoadingButton
                            disabled={emailError != ""}
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, width: '50%', ml: 23 }}
                            loading={isLoading}
                        >
                            send msg
                        </LoadingButton>
                    </Grid>
                </Grid >

            </Box >
            <Divider />
            <Typography sx={{ fontSize: 25, mt: 3, ml: '40%' }} gutterBottom>
                your purchase history
            </Typography>
            {
                orders.map((order, index) => {
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
            <Outlet />
        </>
    );
};
export default Personal;