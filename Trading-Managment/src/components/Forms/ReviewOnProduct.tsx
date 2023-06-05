
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useCallback, useState } from "react";

import AlertDialog from "../Dialog/AlertDialog";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { reviewOnProductFormValues } from "../../types/formsTypes";


import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField, Rating } from "@mui/material";
import { getClientData } from "../../reducers/authSlice";
import { clearStoreError, writeReviewOnProduct } from "../../reducers/storesSlice";


const ReviewOnProduct = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const form = useForm<reviewOnProductFormValues>();
    const [value, setValue] = useState(1);
    const params = useParams();
    const storeId = parseInt(params.storeId ?? "0");
    const productId = parseInt(params.productId ?? "0");
    const orderId = parseInt(params.id ?? "0");
    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);

    const handleOnClose = useCallback(() => {
        navigate(`/dashboard/personal/order/${orderId}`);
        dispatch(getClientData({ userId: userId }));
    }, []);

    const handleOnSubmit = () => {
        form.setValue("userId", userId);
        form.setValue("storeId", storeId);
        form.setValue("productId", productId);
        form.setValue("orderId", orderId);
        form.setValue("rating", value);
        dispatch(writeReviewOnProduct(form.getValues()));
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
                        height: 300,
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
                                please enter the details
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="content"
                                type="text"
                                fullWidth
                                label="content"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('content', {
                                        required: {
                                            value: true,
                                            message: "content is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['content'] ?? false}
                                helperText={form.formState.errors['content']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <Rating
                                name="rating"
                                value={value}
                                onChange={(event, newValue) => {
                                    setValue(newValue ?? 1);
                                }}
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
                                send review
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
export default ReviewOnProduct;