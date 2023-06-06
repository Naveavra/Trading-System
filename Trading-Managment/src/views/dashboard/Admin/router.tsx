import { Route } from "react-router-dom";
import EditMyProfileForm from "../../../components/Forms/EditMyProfile";
import Admin from ".";
import AppointUser from "../../../components/Forms/AppointUser";
import SendMsg from "../../../components/Forms/SendMsg";
import CloseStore from "../../../components/Forms/CloseStore";
import CancelMembership from "../../../components/Forms/CancelMembership";

export const AdminRoutes = [
    <Route key="routes" path="admin" element={<Admin />} >
        <Route path="editMyProfile" element={<EditMyProfileForm />} />
        <Route path="closeStorePerminently" element={<CloseStore />} />
        <Route path="sendMsgToUser" element={<SendMsg />} />
        <Route path="addadmin" element={<AppointUser role={'admin'} />} />
        <Route path="cancelMembership" element={<CancelMembership />} />
    </Route>
]