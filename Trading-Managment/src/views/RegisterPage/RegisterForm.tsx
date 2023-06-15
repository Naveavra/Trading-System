
import { TextField, Typography, Grid, FormControlLabel, Checkbox, CardContent, Alert } from '@mui/material';
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
    const message = useAppSelector((state: RootState) => state.auth.message);
    const error = useAppSelector((state: RootState) => state.auth.error);
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
    const handleOnSubmit = form.handleSubmit(() => {
        dispatch(register(form.getValues())).then((res) => {
            navigate('/auth/login');
        });
    });

    const getDtae = (day: string, month: string, year: string): string => {
        return `${day}/${monthToInt.get(month)}/${year}`;
    }

    const handleDate = (date: React.SetStateAction<Date | null>) => {
        if (date) {
            const arr = date.toString().split(' ');
            form.setValue('birthday', getDtae(arr[2], arr[1], arr[3]));
        }
    }

    return (
        <>
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
            {error && <Alert severity="error">{error}</Alert>}
        </>
    );
};
export default RegisterForm;

