import { Route } from "react-router-dom";
import OrderPage from "../../../components/PersonalPages/OrderPage";

export const ExternalPersonalRoutes =
    <Route key="routes" path="personal"  >
        <Route path="order/:id" element={<OrderPage />} />
    </Route>;