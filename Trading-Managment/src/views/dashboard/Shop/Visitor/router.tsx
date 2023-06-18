import { Route } from "react-router-dom";
import Visitor from "./visitor";
import SendComplain from "../../../../components/Forms/SendComplaint";
import SendQuestion from "../../../../components/Forms/SendQuestion";
import MakeBid from "../../../../components/Forms/Bids/MakeBid";


export const VisitorShopRoutes =
    <Route key="routes" path=":id/visitor" element={<Visitor />} >
        <Route path="sendComplaint" element={<SendComplain />} />
        <Route path="sendQuestion" element={<SendQuestion />} />
        <Route path=":storeId/:productId/makeBid" element={<MakeBid opcode={1} />} />

        {/*todo : add routes here , forms and so*/}
    </Route>
    ;
