

import { useCallback, useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../redux/store";


import AlertDialog from "../../Dialog/AlertDialog";
import { answerBidFormValues } from "../../../types/formsTypes";
import { clearStoreError, getStore } from "../../../reducers/storesSlice";

import { Dialog, Box, Grid, TextField, Typography, Button } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import { answerBid, clearBidError } from "../../../reducers/bidSlice";


const AnswerBid = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<answerBidFormValues>();
    const params = useParams();

    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const error = useAppSelector((state) => state.auth.error);

    const [answer, setAnswer] = useState(0);
    const storeId = parseInt(params.storeId ?? '0');
    const productId = parseInt(params.productId ?? '0');
    const bidId = parseInt(params.bidId ?? '0');

    const hadleAnswer = (answer: boolean) => {
        form.setValue('answer', answer);
        if (answer) {
            setAnswer(1);
        }
        else {
            setAnswer(2);
        }
    }
    //maybe take it from params
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior/bids');
        dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        form.setValue('storeId', storeId);
        form.setValue('bidId', bidId);
        form.setValue('productId', productId);
        dispatch(answerBid(form.getValues()));
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
                                answer
                            </Typography>
                        </Grid>
                        <Box display={'flex'}>
                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, ml: 10 }}
                                onClick={() => hadleAnswer(true)}
                                color={answer === 1 ? 'success' : 'primary'}
                            >
                                accept
                            </Button>
                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, ml: 2 }}
                                onClick={() => hadleAnswer(false)}
                                color={answer === 2 ? 'success' : 'primary'}

                            >
                                decline
                            </Button>
                        </Box>

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
                </Box >
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearBidError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );

}
export default AnswerBid;
