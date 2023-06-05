import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import { useCallback } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { answerComplaintFormValues } from "../../types/formsTypes";
import AlertDialog from "../Dialog/AlertDialog";
import { answerComplaint, clearError, getComplaints } from "../../reducers/adminSlice";

const ComplaintPage = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<answerComplaintFormValues>();

    const params = useParams();
    const complaintId = parseInt(params.id ?? '0');
    const conmplaints = useAppSelector((state) => state.admin.complaints);
    const complaint = conmplaints.find((complaint) => complaint.complaintId == complaintId);

    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const error = useAppSelector((state) => state.auth.error);

    //maybe take it from params
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/admin/seecomplaints');
        dispatch(getComplaints(userId));
    }, []);
    const handleOnSubmit = () => {
        form.setValue('adminId', userId);
        form.setValue('complaintId', complaintId);
        dispatch(answerComplaint(form.getValues()));
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
                                {complaint?.content ?? "no content"}
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
                                answer complaint
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );

}

export default ComplaintPage;