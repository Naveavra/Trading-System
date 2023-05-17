import { Route } from "react-router-dom";
import DashboardPage from "..";
import { SuperiorShopRoutes } from "./Superior/router";
import { VisitorShopRoutes } from "./Visitor/router";
import Shops from ".";

export const ShopRoutes = [
    <Route key="routes" path="shops" >
        <Route path="/dashboard/shops" element={<Shops />} />
        {SuperiorShopRoutes}
        {VisitorShopRoutes}
    </Route>
]