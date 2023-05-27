import { Route } from "react-router-dom";
import { SuperiorShopRoutes } from "./Superior/router";
import { VisitorShopRoutes } from "./Visitor/router";
import { SuperiorPagesShopRoutes } from "./Superior/pageRouter";

export const ShopRoutes = [
    <Route key="routes" path="store" >
        {SuperiorShopRoutes}
        {SuperiorPagesShopRoutes}
        {VisitorShopRoutes}
    </Route>
]