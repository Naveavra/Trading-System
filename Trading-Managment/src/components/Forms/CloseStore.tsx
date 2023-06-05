
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useCallback } from "react";

import AlertDialog from "../Dialog/AlertDialog";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { clearAdminError, closeStorePerminently } from "../../reducers/adminSlice";
import { closeStoreFormValues } from "../../types/formsTypes";


import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import { getClientData } from "../../reducers/authSlice";


const CloseStore = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const form = useForm<closeStoreFormValues>();

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);



    const handleOnClose = useCallback(() => {
        navigate('/dashboard/admin');
        dispatch(getClientData({ userId: userId }));
    }, []);

    const handleOnSubmit = () => {
        form.setValue("userId", userId);
        dispatch(closeStorePerminently(form.getValues()));
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
                                please enter the store id
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="stoer id"
                                type="text"
                                fullWidth
                                label="store id"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('storeId', {
                                        required: {
                                            value: true,
                                            message: "store id is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['storeId'] ?? false}
                                helperText={form.formState.errors['storeId']?.message ?? undefined}
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
                                delete
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
export default CloseStore;