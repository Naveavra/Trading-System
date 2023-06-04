import { Route } from "react-router-dom";
import EditMyProfileForm from "../../../components/Forms/EditMyProfile";
import Admin from ".";
import AppointUser from "../../../components/Forms/AppointUser";
import SendMsg from "../../../components/Forms/SendMsg";

export const AdminRoutes = [
    <Route key="routes" path="admin" element={<Admin />} >
        <Route path="editMyProfile" element={<EditMyProfileForm />} />
        <Route path="closeStorePerminently" element={<EditMyProfileForm />} />
        <Route path="sendMsgToUser" element={<SendMsg />} />
        <Route path="addadmin" element={<AppointUser role={'admin'} />} />
        <Route path="quit" element={<EditMyProfileForm />} />
    </Route>
]