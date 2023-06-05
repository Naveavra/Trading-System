import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField, SelectChangeEvent } from "@mui/material";
import AlertDialog from "../Dialog/AlertDialog";
import { useNavigate } from "react-router-dom";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { useCallback, useEffect, useState } from "react";
import { clearAuthError } from "../../reducers/authSlice";
import { useForm } from "react-hook-form";
import { paymentFormValues } from "../../types/formsTypes";
import { buyCart } from "../../reducers/cartSlice";
import { getSuppliers, getPaymentsService } from "../../reducers/paymentSlice";
import SelectAutoWidth from "../Selectors/AutoWidth";


const BuyCart = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const form = useForm<paymentFormValues>();

    const [payment, setPayment] = useState('');
    const [supplier, setSupplier] = useState('');

    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const suppliers = useAppSelector((state) => state.payment.suppliers);
    const payments = useAppSelector((state) => state.payment.paymentServices);


    const handleChangeSupplier = (event: SelectChangeEvent) => {
        setSupplier(event.target.value as string);
    }
    const handleChangePayment = (event: SelectChangeEvent) => {
        setPayment(event.target.value as string);
    }

    const handleOnClose = useCallback(() => {
        navigate('/dashboard');
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        dispatch(buyCart(form.getValues()))
        handleOnClose();
    }

    useEffect(() => {
        dispatch(getSuppliers());
        dispatch(getPaymentsService());
    }, [dispatch])
    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 700,
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
                                enter the following details
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <SelectAutoWidth label={'payments service'} values={payments} labels={payments} value={payment} handleChange={handleChangePayment} />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="cardNumber"
                                type="text"
                                fullWidth
                                label="card number"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('cardNumber', {
                                        required: {
                                            value: true,
                                            message: "card number id is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['cardNumber'] ?? false}
                                helperText={form.formState.errors['cardNumber']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="month"
                                type="text"
                                fullWidth
                                label="month"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('month', {
                                        required: {
                                            value: true,
                                            message: "month is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['month'] ?? false}
                                helperText={form.formState.errors['month']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="year"
                                type="text"
                                fullWidth
                                label="year"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('year', {
                                        required: {
                                            value: true,
                                            message: "year is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['year'] ?? false}
                                helperText={form.formState.errors['year']?.message ?? undefined}
                            />
                        </Grid>

                        <Grid item xs={12}>
                            <TextField
                                name="holder"
                                type="text"
                                fullWidth
                                label="holder"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('holder', {
                                        required: {
                                            value: true,
                                            message: "holder name is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['holder'] ?? false}
                                helperText={form.formState.errors['holder']?.message ?? undefined}
                            />
                        </Grid>

                        <Grid item xs={12}>
                            <TextField
                                name="ccv"
                                type="text"
                                fullWidth
                                label="ccv"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('ccv', {
                                        required: {
                                            value: true,
                                            message: "ccv is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['ccv'] ?? false}
                                helperText={form.formState.errors['ccv']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="id"
                                type="text"
                                fullWidth
                                label="id"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('id', {
                                        required: {
                                            value: true,
                                            message: "id is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['id'] ?? false}
                                helperText={form.formState.errors['id']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <SelectAutoWidth label={'supplier service'} values={suppliers} labels={suppliers} value={supplier} handleChange={handleChangeSupplier} />
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
                                            value: true,
                                            message: "name id is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['name'] ?? false}
                                helperText={form.formState.errors['name']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="address"
                                type="text"
                                fullWidth
                                label="address"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('address', {
                                        required: {
                                            value: true,
                                            message: "address id is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['address'] ?? false}
                                helperText={form.formState.errors['address']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="city"
                                type="text"
                                fullWidth
                                label="city"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('city', {
                                        required: {
                                            value: true,
                                            message: "city id is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['city'] ?? false}
                                helperText={form.formState.errors['city']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="zip"
                                type="text"
                                fullWidth
                                label="zip"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('zip', {
                                        required: {
                                            value: true,
                                            message: "zip id is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['zip'] ?? false}
                                helperText={form.formState.errors['zip']?.message ?? undefined}
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
                                buy
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
export default BuyCart;