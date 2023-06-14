import { Route } from "react-router-dom";
import StoreOrders from "../../../../components/storePages/StoreOrders";
import MainScreen from "../../../../components/Forms/Discounts/MainScreen";
import CompositeScreen from "../../../../components/Forms/Discounts/CompositeScreen";
import CompositeDiscount from "../../../../components/Forms/Discounts/CompositeDiscount";
import RegularDiscount from "../../../../components/Forms/Discounts/RegularDiscount";
import StoreMessages from "../../../../components/storePages/StoreMessages";
import AnswerQuestion from "../../../../components/Forms/AnswerQuestion";
import BiddingCenter from "../../BiddingCenter/intex";

export const SuperiorPagesShopRoutes =
    <Route key="routes" path="superior">
        <Route path="seestoreorders" element={<StoreOrders />} />
        <Route path="viewmessages" element={<StoreMessages />} />
        <Route path="viewmessages/questions/:id" element={<AnswerQuestion />} />
        <Route path="adddiscountconstraint" element={<MainScreen />} />
        <Route path="conditionalDiscount" element={<CompositeDiscount first={true} />} />
        <Route path="conditionalDiscount/leafs" element={<CompositeScreen />} />
        <Route path="conditionalDiscount/leafs/addNewComposite" element={<CompositeDiscount first={false} />} />
        <Route path="conditionalDiscount/leafs/addNewRegular" element={<RegularDiscount tree={true} />} />
        <Route path="biddingCenter" element={<BiddingCenter />} />
    </Route>
    ;
