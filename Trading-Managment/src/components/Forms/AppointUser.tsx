import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import AlertDialog from "../Dialog/AlertDialog";
import { useForm } from "react-hook-form";
import { appointUserFormValues } from "../../types/formsTypes";
import { useNavigate } from "react-router-dom";
import { appointManager, appointOwner, clearStoreError } from "../../reducers/storesSlice";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { useState } from "react";

interface props {
    role: 'manager' | 'owner';
}
const AppointUser: React.FC<props> = ({ role }) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [open, setOpen] = useState(true);
    const form = useForm<appointUserFormValues>();
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);
    const [emailError, setEmailError] = useState("");
    const handleOnClose = () => {
        setOpen(false);
        navigate(-1);
        // dispatchEvent(getStoreData({}))
    }
    const validateEmail = (email: string): void => {
        const emailRegex: RegExp = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if (!emailRegex.test(email)) {
            form.setError("emailOfUser", { type: 'custom', message: 'email isnt valide' }, { shouldFocus: true })
            setEmailError("email isnt valide")
        }
        else {
            setEmailError("");
            form.clearErrors("emailOfUser");
        }
        // return emailRegex.test(email);
    }
    const handleOnSubmit = () => {
        let response;
        switch (role) {
            case 'manager':
                response = dispatch(appointManager(form.getValues()));
                response.then((res: { meta: { requestStatus: string; }; }) => {
                    if (res.meta.requestStatus === 'fulfilled') {
                        handleOnClose();
                    }
                });
            case 'owner':
                response = dispatch(appointOwner(form.getValues()));
                response.then((res: { meta: { requestStatus: string; }; }) => {
                    if (res.meta.requestStatus === 'fulfilled') {
                        handleOnClose();
                    }
                });
        }
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={open}>
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
                                please enter the email of the person u want to appoint
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="email"
                                type="text"
                                fullWidth
                                label="email"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('emailOfUser', {
                                        required: {
                                            value: true,
                                            message: "email is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['emailOfUser'] ?? false}
                                helperText={form.formState.errors['emailOfUser']?.message ?? undefined}
                                onChange={(e) => {
                                    validateEmail(e.target.value)
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <LoadingButton
                                disabled={emailError != ""}
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                send request
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
export default AppointUser;