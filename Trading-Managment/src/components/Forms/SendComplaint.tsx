

import { useCallback } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";


import AlertDialog from "../Dialog/AlertDialog";
import { sentComplaintFormValues } from "../../types/formsTypes";
import { clearAuthError, sendComplaint } from "../../reducers/authSlice";

import { Dialog, Box, Grid, TextField, Typography } from "@mui/material";
import { LoadingButton } from "@mui/lab";

const SendComplain = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<sentComplaintFormValues>();

    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const error = useAppSelector((state) => state.auth.error);


    const handleOnSubmitComplaint = () => {
        form.setValue('userId', userId);
        dispatch(sendComplaint(form.getValues()));
        handleOnClose();
    }
    const handleOnClose = useCallback(() => {
        navigate(-1);
    }, []);

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
                        onSubmit={handleOnSubmitComplaint}
                    >
                        <Grid item xs={12}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                enter orderId and complaint msg
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="orderId"
                                type="text"
                                fullWidth
                                label="order Id"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('orderId', {
                                        required: {
                                            value: true,
                                            message: "orderId is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['orderId'] ?? false}
                                helperText={form.formState.errors['orderId']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="complaint"
                                type="text"
                                fullWidth
                                label="complaint"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('complaint', {
                                        required: {
                                            value: true,
                                            message: "complaint is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['complaint'] ?? false}
                                helperText={form.formState.errors['complaint']?.message ?? undefined}
                            />
                        </Grid>

                        <Grid item xs={12}>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                send complaint
                            </LoadingButton>
                        </Grid>
                    </Grid >

                </Box >
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );

}
export default SendComplain;
