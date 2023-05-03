import * as React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

//import { FullBackgroundImage } from '../../components/Images/FullBackgroundImage';

import { Box, CardContent, Grid, Typography } from '@mui/material';
import { useAppDispatch, useAppSelector } from '../../redux/store';
import { LoginForm } from './LoginPanel/LoginForm';
import ErrorAlert from '../../components/Alerts/error';
import AlertDialog from '../../components/Dialog/AlertDialog';
import { clearAuthError } from '../../reducers/authSlice';

const LoginPage: React.FC = () => {
    const dispatch = useAppDispatch();
    const isLoggedIn = useAppSelector((state) => !!state.auth.token);
    const error = useAppSelector((state) => state.auth.error);

    return (!isLoggedIn ?
        <>
            <Grid
                spacing={2}
                container
            >
                <Grid item xs={12}>
                    <Typography variant="h2" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                        {'WELCOME TO THE TRAIDING SYSTEM'}
                    </Typography>
                </Grid>
            </Grid >
            {/* <FullBackgroundImage src={bgImage} /> */}
            <Box sx={{
                position: 'fixed',
                display: 'flex',
                left: 0,
                top: 0,
                right: 0,
                bottom: 0,
                alignItems: 'center',
                justifyContent: 'center'
            }}>
                <CardContent sx={{ width: 400 }}>
                    <LoginForm />
                </CardContent>
            </Box>
            {!!error ? <AlertDialog open={!!error} onClose={() => { dispatch(clearAuthError()); }} text={error} sevirity={'error'} /> : null}
        </> :
        <Navigate to="/dashboard" />
    )

};

export default LoginPage;