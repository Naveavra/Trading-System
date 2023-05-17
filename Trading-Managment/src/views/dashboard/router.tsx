import { Route } from "react-router-dom";
import DashboardPage from ".";
import { VisitorShopRoutes } from "./Shop/Visitor/router";
import { SuperiorShopRoutes } from "./Shop/Superior/router";
import ProductDisplay from "../../components/Product/Product";
import { ShopRoutes } from "./Shop/router";
import AddEditStore from "../../components/Forms/AddEditStore";


export const dashboardRoutes = [
    <Route key="routes" path="dashboard" >
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/dashboard/store/new" element={<AddEditStore mode={"add"} />} />
        {/*todo : add routes here*/}
        {ShopRoutes}
    </Route>

];

