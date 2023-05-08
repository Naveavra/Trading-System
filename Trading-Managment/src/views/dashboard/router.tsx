import { Route } from "react-router-dom";
import DashboardPage from ".";
import { VisitorShopRoutes } from "./Shop/Visitor/router";
import { SuperiorShopRoutes } from "./Shop/Superior/router";
import ProductDisplay from "../../components/Products/Products";


export default [
    <Route key="routes" path="dashboard" element={<ProductDisplay />}>
        {/*todo : add routes here*/}
        {SuperiorShopRoutes}
        {VisitorShopRoutes}
    </Route>

];