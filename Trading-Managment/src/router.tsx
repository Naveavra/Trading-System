import {
    createBrowserRouter,
    createRoutesFromElements,
    Route,
    Navigate,
} from 'react-router-dom';

import dashboardRoutes from '../src/views/dashboard/router';
import loginRoutes from '../src/views/LoginPage/router';

import ErrorPage from './views/dashboard/Error';

export const router = createBrowserRouter(
    createRoutesFromElements(
        <>
            <Route path="/" element={<Navigate to="/auth/login" />} />
            <Route path="*" element={<ErrorPage />} />

            {dashboardRoutes}
            {loginRoutes}
        </>
    )
);