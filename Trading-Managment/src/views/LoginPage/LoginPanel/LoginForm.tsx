
import { TextField, Typography, Grid, FormControlLabel, Checkbox, CardContent } from '@mui/material';
import { useForm, Controller } from 'react-hook-form';
import { LoadingButton } from '@mui/lab';

import { useAppDispatch, useAppSelector, RootState } from '../../../redux/store';
import { LoginFormValues } from '../types';
import { login } from '../../../reducers/authSlice';
import { useAppStorage } from '../../../hooks/useAppStorage';
import { localStorage } from '../../../config';
import { useNavigate } from 'react-router-dom';

interface LoginFormProps {

};

export const LoginForm = ({ }: LoginFormProps) => {
    const [defaultChecked, setDefaultChecked] = useAppStorage(localStorage.settings.login_page.remember_me);
    const dispatch = useAppDispatch();
    const isLoginLoading = useAppSelector((state: RootState) => state.auth.isLoginLoading);
    const form = useForm<LoginFormValues>();
    const navigate = useNavigate();
    // Form Buttons
    const handleOnSubmit = form.handleSubmit(() => {
        setDefaultChecked(form.getValues().rememberMe);
        dispatch(login(form.getValues()));
    });
    const handleOnRegster = () => {
        navigate('/auth/register');
    };
    const handleOnForgotPassword = () => {
        navigate('/auth/forgot-password');
    };
    const handleOnContinueAsGuest = () => {
        navigate('/dashboard');
    };

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
                <FormControlLabel control={
                    <Controller
                        name={'rememberMe'}
                        control={form.control}
                        defaultValue={defaultChecked}
                        render={({ field, fieldState, formState }) => (
                            <Checkbox
                                checked={field.value}
                                onChange={(e) => field.onChange(e.target.checked)}
                            />
                        )}
                    />
                } label="remember me" />
            </Grid>

            <Grid item xs={12}>
                <LoadingButton
                    type="submit"
                    fullWidth
                    variant="contained"
                    loading={isLoginLoading}
                    sx={{ color: 'white', '&:hover': { backgroundColor: 'green' } }}
                >
                    {'login'}
                </LoadingButton >
            </Grid>
            <Grid item xs={12}>
                <LoadingButton
                    type="submit"
                    fullWidth
                    variant="contained"
                    onClick={handleOnContinueAsGuest}
                    sx={{ color: 'white', '&:hover': { backgroundColor: 'green' }, width: '50%', right: 5 }}
                >
                    {'continue as guest'}
                </LoadingButton >
            </Grid>
            <Grid item xs={12}>
                <LoadingButton
                    type="submit"
                    fullWidth
                    variant="contained"
                    onClick={handleOnRegster}
                    sx={{ color: 'white', '&:hover': { backgroundColor: 'green' }, width: '50%', left: 205, bottom: 53 }}
                >
                    {'register'}
                </LoadingButton >
            </Grid>
        </Grid >

    );
};