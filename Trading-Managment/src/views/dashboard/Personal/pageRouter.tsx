import { Route } from "react-router-dom";
import OrderPage from "../../../components/PersonalPages/OrderPage";
import ReviewOnStore from "../../../components/Forms/ReviewOnStore";
import ReviewOnProduct from "../../../components/Forms/ReviewOnProduct";

export const ExternalPersonalRoutes =
    <Route key="routes" path="personal"  >
        <Route path="order/:id" element={<OrderPage />} />
        <Route path="order/:id/reviewStore" element={<ReviewOnStore />} />
        <Route path="order/:id/reviewProduct/:storeId/:productId" element={<ReviewOnProduct />} />

    </Route>;