import { Route } from "react-router-dom";
import Superior from "./superior";
import StoreOrders from "../../../../components/storePages/StoreOrders";
import Bar2 from "../../../../components/Bars/Navbar/NavBar2";

export const SuperiorPagesShopRoutes =
    <Route key="routes" path="superior">
        <Route path="seestoreorders" element={<StoreOrders />} />
    </Route>
    ;
