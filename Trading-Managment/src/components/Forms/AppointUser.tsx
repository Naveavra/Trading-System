import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useCallback, useState } from "react";

import AlertDialog from "../Dialog/AlertDialog";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { appointAdmin } from "../../reducers/adminSlice";
import { appointManager, appointOwner, clearStoreError, getStore } from "../../reducers/storesSlice";
import { addAdminFormValues, appointUserFormValues } from "../../types/formsTypes";


import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";



interface props {
    role: 'manager' | 'owner' | 'admin';
}
const AppointUser: React.FC<props> = ({ role }) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const form = useForm<appointUserFormValues>();
    const form2 = useForm<addAdminFormValues>();

    const [emailError, setEmailError] = useState("");
    const [newPasswordError, setNewPasswordError] = useState("");

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const storeId = useAppSelector((state: RootState) => state.store.storeState.watchedStore.storeId);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);



    const handleOnClose = useCallback(() => {
        if (role != 'admin') {
            navigate('/dashboard/store/superior');
            dispatch(getStore({ userId: userId, storeId: storeId }));
        }
        else {
            navigate('/dashboard/admin');
            //dispatch(getAdminData({}))
        }
    }, []);
    const validateEmail = (email: string): void => {
        const emailRegex: RegExp = /^\w+([\.-]?\w+)*@\w+([\.-]?\w\+)*(\.\w{2,3})+$/;
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
    const validatePassword = (password: string): void => {
        const hasUppercase = /[A-Z]/.test(password);
        const hasLowercase = /[a-z]/.test(password);
        const hasNumber = /\d/.test(password);
        const isLengthValid = password.length >= 6 && password.length <= 20;

        const legal = hasUppercase && hasLowercase && hasNumber && isLengthValid;
        if (!legal) {
            console.log("failed");
            form2.setError("password", { type: 'custom', message: 'password must containce one appercase , one smallercase , one number , and legth between 6 and 20' }, { shouldFocus: true })
            setNewPasswordError("password isnt valide");
        }
        else {
            setNewPasswordError("");
            form2.clearErrors("password");
            console.log(form.formState.errors);
        }
    }
    const handleOnSubmit = () => {
        form.setValue("storeId", storeId);
        form.setValue("userId", userId);
        form2.setValue("userId", userId);
        switch (role) {
            case 'manager':
                dispatch(appointManager(form.getValues()));
                break;
            case 'owner':
                dispatch(appointOwner(form.getValues()));
                break;
            case 'admin':
                dispatch(appointAdmin(form2.getValues()));
                break;
            default:
                break;
        }
        handleOnClose();
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
                {role === 'admin' ?
                    <Box
                        sx={{
                            marginTop: 4,
                            top: '50%',
                            left: '50%',
                            height: 350,
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
                                    please enter the email & password of the new admin
                                </Typography>
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    name="email"
                                    type="text"
                                    fullWidth
                                    label="name"
                                    sx={{ mt: 1, mb: 1 }}
                                    inputProps={{
                                        ...form2.register('email', {
                                            required: {
                                                value: true,
                                                message: "name is required"
                                            }
                                        })
                                    }}
                                    error={!!form2.formState.errors['email'] ?? false}
                                    helperText={form2.formState.errors['email']?.message ?? undefined}
                                    onChange={(e) => {
                                        validateEmail(e.target.value)
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    name="password"
                                    type="password"
                                    fullWidth
                                    label="password"
                                    sx={{ mt: 1, mb: 1 }}
                                    inputProps={{
                                        ...form2.register('password', {
                                            required: {
                                                value: true,
                                                message: "password is mendatory"
                                            }
                                        })
                                    }}
                                    error={!!form2.formState.errors['password'] ?? false}
                                    helperText={form2.formState.errors['password']?.message ?? undefined}
                                    onChange={(e) => {
                                        validatePassword(e.target.value)
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <LoadingButton
                                    disabled={newPasswordError != ""}
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{ mt: 3, mb: 2 }}
                                    loading={isLoading}
                                >
                                    add admin
                                </LoadingButton>
                            </Grid>
                        </Grid >
                    </Box>
                    :
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
                }
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearStoreError({})); }} text={error} sevirity={"error"} />
                : null}
        </>
    );
}
export default AppointUser;