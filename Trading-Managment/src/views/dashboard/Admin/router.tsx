import { Route } from "react-router-dom";
import EditMyProfileForm from "../../../components/Forms/EditMyProfile";
import Admin from ".";

export const AdminRoutes = [
    <Route key="routes" path="admin" element={<Admin />} >
        <Route path="editMyProfile" element={<EditMyProfileForm />} />
    </Route>
]