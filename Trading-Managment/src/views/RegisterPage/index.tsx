import * as React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

//import { FullBackgroundImage } from '../../components/Images/FullBackgroundImage';

import { Box, CardContent, Grid, Typography } from '@mui/material';
import { useAppSelector } from '../../redux/store';
import RegisterForm from './RegisterForm';


const RegisterPage: React.FC = () => {
    const isLoggedIn = useAppSelector((state) => !!state.auth.token);
    const userId = useAppSelector((state) => state.auth.userId);
    console.log('isLoggedIn', isLoggedIn);
    return (userId == 0 ?

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
                    <RegisterForm />
                </CardContent>
            </Box>
        </>
        :
        <Navigate to="/dashboard" />
    )

};

export default RegisterPage;