import { Route } from "react-router-dom";
import DashboardPage from ".";
import { ShopRoutes } from "./Shop/router";
import AddEditStore from "../../components/Forms/AddEditStore";
import Cart from "../../components/Cart/Cart";
import { PersonalRoutes } from "./Personal/router";
import { AdminRoutes } from "./Admin/router";
import SendMsg from "../../components/Forms/SendMsg";
import SendComplain from "../../components/Forms/SendComplaint";
import { ExternalAdminRoutes } from "./Admin/pageRouter";
import BuyCart from "../../components/Forms/BuyCartForm";
import { ExternalPersonalRoutes } from "./Personal/pageRouter";
import MakeBid from '../../components/Forms/Bids/MakeBid'
import BiddingSenter from "./BiddingCenter/intex";
import { BiddingRoutes } from "./BiddingCenter/router";


export const dashboardRoutes = [
    <Route key="routes" path="dashboard" >
        <Route path="" element={<DashboardPage />} />
        <Route path="cart" element={<Cart />} />
        <Route path="cart/payment" element={<BuyCart personal={false} />} />
        <Route path="store/new" element={<AddEditStore mode={"add"} />} />
        <Route path="sendMsg" element={<SendMsg />} />
        <Route path="sendComplaint" element={<SendComplain />} />
        <Route path=":storeId/:productId/makeBid" element={<MakeBid opcode={0} />} />
        {/* <Route path="biddingSenter" element={<BiddingSenter />} /> */}


        {/*todo : add routes here*/}
        {AdminRoutes}
        {BiddingRoutes}
        {PersonalRoutes}
        {ShopRoutes}
        {ExternalAdminRoutes}
        {ExternalPersonalRoutes}
    </Route>

];

