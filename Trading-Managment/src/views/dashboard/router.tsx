import { Route } from "react-router-dom";
import DashboardPage from ".";
import { VisitorShopRoutes } from "./Shop/Visitor/router";
import { SuperiorShopRoutes } from "./Shop/Superior/router";
import ProductDisplay from "../../components/Products/Product";
import { ShopRoutes } from "./Shop/router";


export const dashboardRoutes = [
    <Route key="routes" path="dashboard" >
        <Route path="/dashboard" element={<DashboardPage />} />
        {/*todo : add routes here*/}
        {ShopRoutes}
    </Route>

];
{/* <Route key="routes" path="/auth" >
<Route path="login" element={<LoginPage />} />
<Route path="register" element={<RegisterPage />} />
{/* <Route path="forgot-password" element={<ForgotPasswordPage />} /> */}
//</Route> */}
