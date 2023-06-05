import { Route } from "react-router-dom";
import Complaints from "../../../components/AdminPages/Complaints";
import Complaint from "../../../components/Forms/Complaint";

export const ExternalAdminRoutes =
    <Route key="routes" path="admin"  >
        <Route path="seecomplaints" element={<Complaints />} />
        <Route path="seecomplaints/:id" element={<Complaint />} />
    </Route>;