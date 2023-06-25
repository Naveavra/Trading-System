

import { useCallback, useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../redux/store";


import AlertDialog from "../../Dialog/AlertDialog";
import { makeBidFormValues } from "../../../types/formsTypes";
import { clearStoreError } from "../../../reducers/storesSlice";
import { sendMessage } from "../../../reducers/authSlice";

import { Dialog, Box, Grid, TextField, Typography } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import { addBid, clearBidError } from "../../../reducers/bidSlice";

interface MakeBidProps {
    opcode: number;
}
const MakeBid: React.FC<MakeBidProps> = ({ opcode }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<makeBidFormValues>();
    const params = useParams();

    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.store.storeState.isLoading);
    const error = useAppSelector((state) => state.auth.error);

    const storeId = parseInt(params.storeId ?? '0');
    const productId = parseInt(params.productId ?? '0');

    //maybe take it from params
    const handleOnClose = useCallback(() => {
        switch (opcode) {
            case 0:
                navigate('/dashboard');
                break;
            case 1:
                navigate(`/dashboard/store/${storeId}/visitor`);
                break;
            case 2:
                navigate('/dashboard/store/superior');
                break;
            default:
                break;
        }
    }, []);
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        form.setValue('storeId', storeId);
        form.setValue('productId', productId);
        dispatch(addBid(form.getValues()));
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
                                enter your bid details
                            </Typography>
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
                                            value: true,
                                            message: "price is required"
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
                                            value: true,
                                            message: "quantity is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['quantity'] ?? false}
                                helperText={form.formState.errors['quantity']?.message ?? undefined}

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
                                send
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearBidError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );

}
export default MakeBid;
