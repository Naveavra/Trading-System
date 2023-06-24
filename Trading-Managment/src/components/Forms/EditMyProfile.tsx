import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import AlertDialog from "../Dialog/AlertDialog";
import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { editProfileFormValues } from "../../types/formsTypes";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { clearProductError } from "../../reducers/productsSlice";
import { clearAuthError, editProfile, getClientData, ping } from "../../reducers/authSlice";
import { DatePicker } from "@mui/x-date-pickers";



const EditMyProfileForm = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<editProfileFormValues>();

    const userId = useAppSelector((state) => state.auth.userId);
    const error = useAppSelector((state) => state.auth.error);
    const isLoading = useAppSelector((state) => state.product.isLoading)
    const [emailError, setEmailError] = useState("");


    const monthToInt = new Map<string, string>([
        ['Jan', '01'],
        ['Feb', '02'],
        ['Mar', '03'],
        ['Apr', '04'],
        ['May', '05'],
        ['Jun', '06'],
        ['Jul', '07'],
        ['Aug', '08'],
        ['Sep', '09'],
        ['Oct', '10'],
        ['Nov', '11'],
        ['Dec', '12'],
    ]);
    // Form Buttons

    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        dispatch(editProfile(form.getValues()));
        handleOnClose();
    };
    const handleOnClose = useCallback(() => {
        navigate("/dashboard/personal");
        dispatch(getClientData({ userId: userId }));
        // navigate(-1);
    }, []);

    const getDtae = (day: string, month: string, year: string): string => {
        return `${day}/${monthToInt.get(month)}/${year}`;
    }

    const handleDate = (date: React.SetStateAction<Date | null>) => {
        if (date) {
            const arr = date.toString().split(' ');
            form.setValue('birthday', getDtae(arr[2], arr[1], arr[3]));
        }

    }

    const validateEmail = (email: string): void => {
        const emailRegex: RegExp = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if (!emailRegex.test(email)) {
            form.setError("email", { type: 'custom', message: 'email isnt valide' }, { shouldFocus: true })
            setEmailError("email isnt valide")
        }
        else {
            setEmailError("");
            form.clearErrors("email");
        }
        // return emailRegex.test(email);
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
                                <b>update details</b>
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
                                    ...form.register('email', {
                                        required: {
                                            value: false,
                                            message: "not mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['email'] ?? false}
                                helperText={form.formState.errors['email']?.message ?? undefined}
                                onChange={(e) => {
                                    validateEmail(e.target.value)
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <DatePicker label={'birthday'} onChange={handleDate} sx={{ width: '100%' }} />
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
                                update
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()); }} text={error} sevirity={"error"} />
                : null
            }
        </>
    );
}
export default EditMyProfileForm;