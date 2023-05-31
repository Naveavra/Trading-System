import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import AlertDialog from "../Dialog/AlertDialog";
import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { editProfileFormValues } from "../../types/formsTypes";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { clearProductError } from "../../reducers/productsSlice";
import { editProfile, getClientData, ping } from "../../reducers/authSlice";



const EditMyProfileForm = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<editProfileFormValues>();

    const userId = useAppSelector((state) => state.auth.userId);
    const token = useAppSelector((state) => state.auth.token);
    const error = useAppSelector((state) => state.auth.error);
    const isLoading = useAppSelector((state) => state.product.isLoading)
    const [emailError, setEmailError] = useState("");

    const PING_INTERVAL = 10000; // 10 seconds in milliseconds

    const handleOnSubmit = () => {
        let response;
        form.setValue('userId', userId);
        response = dispatch(editProfile(form.getValues()));
        handleOnClose();

    };
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/personal');
        dispatch(getClientData({ userId: userId }));
        // navigate(-1);
    }, []);

    const sendPing = () => {
        if (userId != 0) {
            dispatch(ping(userId));
        }
    }

    useEffect(() => {
        // Call the sendPing function every 2 seconds
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };

    }, [])
    const validateEmail = (email: string): void => {
        const emailRegex: RegExp = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if (!emailRegex.test(email)) {
            form.setError("email", { type: 'custom', message: 'email isnt valide' }, { shouldFocus: true })
            setEmailError("email isnt valide")
        }
        else {
            setEmailError("");
            form.clearErrors("email");
        }
        // return emailRegex.test(email);
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 520,
                        width: '80%',
                        flexDirection: 'column',
                        alignItems: 'center',
                        marginLeft: 'auto',
                        marginRight: 'auto',
                        marginBottom: -2,
                        bgcolor: 'background.paper',

                    }}
                >
                    <Grid
                        spacing={2}
                        container
                        component="form"
                        onSubmit={handleOnSubmit}
                    >
                        <Grid item xs={12}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                <b>update details</b>
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="name"
                                type="text"
                                fullWidth
                                label="name"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('name', {
                                        required: {
                                            value: false,
                                            message: "name is not required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['name'] ?? false}
                                helperText={form.formState.errors['name']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="email"
                                type="text"
                                fullWidth
                                label="email"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('email', {
                                        required: {
                                            value: false,
                                            message: "not mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['email'] ?? false}
                                helperText={form.formState.errors['email']?.message ?? undefined}
                                onChange={(e) => {
                                    validateEmail(e.target.value)
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="age"
                                type="text"
                                fullWidth
                                label="age"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('age', {
                                        required: {
                                            value: false,
                                            message: "not mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['age'] ?? false}
                                helperText={form.formState.errors['age']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="birthday"
                                type="text"
                                fullWidth
                                label="birthday"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('birthday', {
                                        required: {
                                            value: false,
                                            message: "birthday is not mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['birthday'] ?? false}
                                helperText={form.formState.errors['birthday']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <LoadingButton
                                disabled={emailError != ""}
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                update
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearProductError()); }} text={error} sevirity={"error"} />
                : null
            }
        </>
    );
}
export default EditMyProfileForm;