

import { useCallback } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";


import AlertDialog from "../Dialog/AlertDialog";
import { sendQuestionFormValues, sentComplaintFormValues } from "../../types/formsTypes";
import { clearAuthError, sendComplaint } from "../../reducers/authSlice";

import { Dialog, Box, Grid, TextField, Typography } from "@mui/material";
import { LoadingButton } from "@mui/lab";
import { writeQuestion } from "../../reducers/storesSlice";

const SendQuestion = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<sendQuestionFormValues>();
    const params = useParams();

    const storeId = parseInt(params.id ?? '0');
    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const error = useAppSelector((state) => state.auth.error);


    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        form.setValue('storeId', storeId);
        dispatch(writeQuestion(form.getValues()));
        handleOnClose();
    }
    const handleOnClose = useCallback(() => {
        navigate(`/dashboard/store/${storeId}/visitor`);
    }, []);

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
                                enter queston but not not something hard , we arrent chatgpt ni***
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="question"
                                type="text"
                                fullWidth
                                label="question"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('question', {
                                        required: {
                                            value: true,
                                            message: "orderId is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['question'] ?? false}
                                helperText={form.formState.errors['question']?.message ?? undefined}
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
                                send question
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
export default SendQuestion;
