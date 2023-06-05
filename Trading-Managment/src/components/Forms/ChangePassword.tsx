import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import { useForm } from "react-hook-form";
import { changePasswordFormValues } from "../../types/formsTypes";
import AlertDialog from "../Dialog/AlertDialog";
import { useCallback, useEffect, useState } from "react";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { changePassword, clearAuthError, getClientData } from "../../reducers/authSlice";


const ChangePassword = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const form = useForm<changePasswordFormValues>();

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);
    const [oldPasswordError, setOldPasswordError] = useState("");
    const [newPasswordError, setNewPasswordError] = useState("");

    const handleOnSubmit = async () => {
        form.setValue('userId', userId);
        dispatch(changePassword(form.getValues()));
        handleOnClose();
    }
    const handleOnClose = useCallback(() => {
        navigate(-1);
        dispatch(getClientData({ userId: userId }));
    }, []);

    const validatePassword = (password: string, opcode: number): void => {
        const hasUppercase = /[A-Z]/.test(password);
        const hasLowercase = /[a-z]/.test(password);
        const hasNumber = /\d/.test(password);
        const isLengthValid = password.length >= 6 && password.length <= 20;

        const legal = hasUppercase && hasLowercase && hasNumber && isLengthValid;
        if (!legal) {
            console.log("failed");
            if (opcode == 1) {
                form.setError("newPassword", { type: 'custom', message: 'password must containce one appercase , one smallercase , one number , and legth between 6 and 20' }, { shouldFocus: true })
                setNewPasswordError("new password isnt valide")
            }
            else {
                form.setError("oldPassword", { type: 'custom', message: 'password must containce one appercase , one smallercase , one number , and legth between 6 and 20' }, { shouldFocus: true })
                setOldPasswordError("old password isnt valide")
            }

        }
        else {
            if (opcode == 1) {
                setNewPasswordError("");
                form.clearErrors("newPassword");
                console.log(form.formState.errors)
            }
            else {
                setOldPasswordError("");
                form.clearErrors("oldPassword");
                console.log(form.formState.errors)
            }
        }
    }
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds
    // Send a ping to the server
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
        // Call the sendPing function every 2 seconds
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };

    }, [])

    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 350,
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
                                <span>change password</span>
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="old password"
                                type="password"
                                fullWidth
                                label="old password"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('oldPassword', {
                                        required: {
                                            value: true,
                                            message: "oldPassword is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['oldPassword'] ?? false}
                                helperText={form.formState.errors['oldPassword']?.message ?? undefined}
                                onChange={(e) => {
                                    validatePassword(e.target.value, 0)
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="new password"
                                type="password"
                                fullWidth
                                label="new password"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('newPassword', {
                                        required: {
                                            value: true,
                                            message: "descreaption is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['newPassword'] ?? false}
                                helperText={form.formState.errors['newPassword']?.message ?? undefined}
                                onChange={(e) => {
                                    validatePassword(e.target.value, 1)
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <LoadingButton
                                disabled={oldPasswordError != "" || newPasswordError != ""}
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                change password
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );
}
export default ChangePassword;