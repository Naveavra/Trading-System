import { Route } from "react-router-dom";
import Visitor from "./visitor";
import SendComplain from "../../../../components/Forms/SendComplaint";
import SendQuestion from "../../../../components/Forms/SendQuestion";


export const VisitorShopRoutes =
    <Route key="routes" path=":id/visitor" element={<Visitor />} >
        <Route path="sendComplaint" element={<SendComplain />} />
        <Route path="sendQuestion" element={<SendQuestion />} />

        {/*todo : add routes here , forms and so*/}
    </Route>
    ;
