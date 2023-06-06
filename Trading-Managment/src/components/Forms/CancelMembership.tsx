
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useCallback } from "react";

import AlertDialog from "../Dialog/AlertDialog";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { cancelMembership, clearAdminError, removeUser } from "../../reducers/adminSlice";
import { cancelMembershipFormValues } from "../../types/formsTypes";


import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import { getClientData } from "../../reducers/authSlice";


const CancelMembership = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const form = useForm<cancelMembershipFormValues>();

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);



    const handleOnClose = useCallback(() => {
        navigate('/dashboard/admin');
        dispatch(getClientData({ userId: userId }));
    }, []);

    const handleOnSubmit = () => {
        form.setValue("userId", userId);
        dispatch(cancelMembership(form.getValues()));
        dispatch(removeUser(form.getValues('userName')));
        handleOnClose();
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 250,
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
                                please enter the user name
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="user name "
                                type="text"
                                fullWidth
                                label="user name "
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('userName', {
                                        required: {
                                            value: true,
                                            message: "user name  is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['userName'] ?? false}
                                helperText={form.formState.errors['userName']?.message ?? undefined}
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
                                cancel membership
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>

            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearAdminError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );
}
export default CancelMembership;