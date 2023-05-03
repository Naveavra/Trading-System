
import { TextField, Typography, Grid, FormControlLabel, Checkbox, CardContent } from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { LoadingButton } from '@mui/lab';

import { useAppDispatch, useAppSelector, RootState } from '../../redux/store';

import { useNavigate } from 'react-router-dom';
import { RegisterPostData } from '../../types/requestTypes/authTypes';
import { register } from '../../reducers/authSlice';
import { DatePicker } from '@mui/x-date-pickers';


export const RegisterForm: React.FC = () => {
    //const [defaultChecked, setDefaultChecked] = useAppStorage(localStorage.settings.login_page.remember_me);
    const dispatch = useAppDispatch();
    const isregisterLoading = useAppSelector((state: RootState) => state.auth.isRegisterLoading);
    const form = useForm<RegisterPostData>();
    const navigate = useNavigate();
    // Form Buttons
    const handleOnSubmit = form.handleSubmit(() => {

        dispatch(register(form.getValues()));
        navigate(-1);
    });

    const handleDate = (date: React.SetStateAction<Date | null>) => {
        console.log(date?.toLocaleString());

    }

    return (

        <Grid
            spacing={2}
            container
            component="form"
            onSubmit={handleOnSubmit}
        >

            <Grid item xs={12}>
                <TextField
                    name="email"
                    type="text"
                    fullWidth
                    label="user email"
                    inputProps={{
                        ...form.register('email', {
                            required: {
                                value: true,
                                message: "fill email"
                            }
                        })
                    }}
                    required
                    error={!!form.formState.errors['email'] ?? false}
                    helperText={form.formState.errors['email']?.message ?? undefined}
                />
            </Grid>
            <Grid item xs={12}>
                <TextField
                    name="password"
                    type="password"
                    fullWidth
                    label="password"
                    inputProps={{
                        ...form.register('password', {
                            required: {
                                value: true,
                                message: "fill password"
                            }
                        })
                    }}
                    required
                    error={!!form.formState.errors['password'] ?? false}
                    helperText={form.formState.errors['password']?.message ?? undefined}
                />
            </Grid>

            <Grid item xs={12}>
                <DatePicker label={'birthday'} onChange={handleDate} sx={{ width: '100%' }} />
            </Grid>
            <Grid item xs={12}>
                <LoadingButton
                    type="submit"
                    fullWidth
                    variant="contained"
                    loading={isregisterLoading}
                    sx={{ color: 'white', '&:hover': { backgroundColor: 'green' } }}
                >
                    {'register'}
                </LoadingButton >
            </Grid>
        </Grid >

    );
};
export default RegisterForm;