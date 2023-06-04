

import { useCallback, useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";


import AlertDialog from "../Dialog/AlertDialog";
import { sendMsgFormValues } from "../../types/formsTypes";
import { clearStoreError } from "../../reducers/storesSlice";
import { sendMessage } from "../../reducers/authSlice";

import { Dialog, Box, Grid, TextField, Typography } from "@mui/material";
import { LoadingButton } from "@mui/lab";

const SendMsg = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<sendMsgFormValues>();
    const [emailError, setEmailError] = useState("");

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
    }
    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const error = useAppSelector((state) => state.auth.error);

    //maybe take it from params
    const handleOnClose = useCallback(() => {
        navigate(-1);
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        dispatch(sendMessage(form.getValues()));
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 330,
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
                                "Sometimes silence is stronger than words..."
                            </Typography>
                        </Grid>
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
                                            message: "complaint is mendatory"
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
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                send msg
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearStoreError({})); }} text={error} sevirity={"error"} />
                : null}
        </>
    );

}
export default SendMsg;
