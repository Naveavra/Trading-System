import { Route } from 'react-router-dom';
import LoginPage from '.';
import RegisterPage from '../RegisterPage';




export default [
    <Route key="routes" path="/auth" >
        <Route path="login" element={<LoginPage />} />
        <Route path="register" element={<RegisterPage />} />
        {/* <Route path="forgot-password" element={<ForgotPasswordPage />} /> */}
    </Route>

];