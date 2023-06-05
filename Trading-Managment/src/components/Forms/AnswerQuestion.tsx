import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import { useCallback } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { answerQuestionFormValues } from "../../types/formsTypes";
import AlertDialog from "../Dialog/AlertDialog";
import { answerComplaint, clearError, getComplaints } from "../../reducers/adminSlice";
import { answerQuestion, clearStoreError } from "../../reducers/storesSlice";

const AnswerQuestion = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<answerQuestionFormValues>();


    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const error = useAppSelector((state) => state.auth.error);

    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const params = useParams();
    const questionId = parseInt(params.id ?? '0');
    const questions = useAppSelector((state) => state.store.storeState.watchedStore.questions);
    const question = questions.find((complaint) => complaint.questionId == questionId);



    //maybe take it from params
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior/viewmessages');
        dispatch(getComplaints(userId));
    }, []);
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        form.setValue('questionId', questionId);
        form.setValue('storeId', storeId);
        dispatch(answerQuestion(form.getValues()));
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
                                answer complaint
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                {question?.content ?? "no content"}
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="content"
                                type="text"
                                fullWidth
                                label="answer"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('answer', {
                                        required: {
                                            value: true,
                                            message: "answer is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['answer'] ?? false}
                                helperText={form.formState.errors['answer']?.message ?? undefined}
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
                                answer questionId
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

export default AnswerQuestion;