import { Route } from "react-router-dom";
import StoreOrders from "../../../../components/storePages/StoreOrders";
import MainScreen from "../../../../components/Forms/Discounts/MainScreen";
import CompositeScreen from "../../../../components/Forms/Discounts/CompositeScreen";
import CompositeDiscount from "../../../../components/Forms/Discounts/CompositeDiscount";

export const SuperiorPagesShopRoutes =
    <Route key="routes" path="superior">
        <Route path="seestoreorders" element={<StoreOrders />} />
        <Route path="adddiscountconstraint" element={<MainScreen />} />
        <Route path="conditionalDiscount" element={<CompositeDiscount first={true} />} />
        <Route path="conditionalDiscount/leafs" element={<CompositeScreen />} />
        <Route path="conditionalDiscount/leafs/addNewComposite" element={<CompositeDiscount first={false} />} />
    </Route>
    ;
