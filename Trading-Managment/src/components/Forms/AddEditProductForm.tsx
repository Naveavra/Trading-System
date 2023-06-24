import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { type } from "os";
import error from "../Alerts/error";
import AlertDialog from "../Dialog/AlertDialog";
import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import { ProductFormValues } from "../../types/formsTypes";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { clearProductError, getProducts, patchProduct, postProduct } from "../../reducers/productsSlice";
import SuccessAlert from "../Alerts/success";
import { getNotifications, ping } from "../../reducers/authSlice";
import { getStore } from "../../reducers/storesSlice";

interface Props {
    mode: 'add' | 'edit';
}

const AddEditProductForm: React.FC<Props> = ({ mode }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const params = useParams();
    const form = useForm<ProductFormValues>();

    const required = (() => { return mode === 'add' ? true : false })();
    const userId = useAppSelector((state) => state.auth.userId);

    const productId = params?.productId;
    const error = useAppSelector((state) => state.store.storeState.error);
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const isLoading = useAppSelector((state) => state.product.isLoading)



    const handleOnSubmit = () => {
        //todo : handle null in edit
        let response;
        form.setValue('storeId', storeId);
        form.setValue('id', userId);
        switch (mode) {
            case 'add':
                response = dispatch(postProduct(form.getValues()));
                break;
            case 'edit':
                form.setValue('productId', parseInt(productId ?? '-1'));
                response = dispatch(patchProduct(form.getValues()));
                break;
            default:
                break;
        }
        handleOnClose();
    };
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior');
        dispatch(getStore({ userId: userId, storeId: storeId }));
        dispatch(getProducts());
        dispatch(getStore({ userId: userId, storeId: storeId }));
        dispatch(getProducts());
    }, []);

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
                                {mode === 'add' ? <b>please enter new products's details</b> : <b>update product</b>}
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            {/* <SelectAutoWidth label={label} values={types_names} labels={types_names} value={type} handleChange={handleSetType} /> */}
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
                                            value: required,
                                            message: "name is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['name'] ?? false}
                                helperText={form.formState.errors['name']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="category"
                                type="text"
                                fullWidth
                                label="category"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('category', {
                                        required: {
                                            value: required,
                                            message: "not mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['category'] ?? false}
                                helperText={form.formState.errors['category']?.message ?? undefined}
                            />
                        </Grid>

                        <Grid item xs={12}>
                            <TextField
                                name="description"
                                type="text"
                                fullWidth
                                label="description"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('description', {
                                        required: {
                                            value: required,
                                            message: "descreaption is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['description'] ?? false}
                                helperText={form.formState.errors['description']?.message ?? undefined}
                            />
                        </Grid>

                        <Grid item xs={12}>
                            <TextField
                                name="price"
                                type="text"
                                fullWidth
                                label="price"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('price', {
                                        required: {
                                            value: required,
                                            message: "price is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['price'] ?? false}
                                helperText={form.formState.errors['price']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="quantity"
                                type="text"
                                fullWidth
                                label="quantity"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('quantity', {
                                        required: {
                                            value: required,
                                            message: "quantity is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['quantity'] ?? false}
                                helperText={form.formState.errors['quantity']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="img"
                                type="text"
                                fullWidth
                                label="img"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('img', {
                                        required: {
                                            value: required,
                                            message: "img is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['img'] ?? false}
                                helperText={form.formState.errors['img']?.message ?? undefined}
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
                                {mode === 'add' ? 'הוסף' : 'עדכן'}
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
export default AddEditProductForm;