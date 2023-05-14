import { Route } from "react-router-dom";
import Superior from "./superior";
import AddEditProductForm from "../../../../components/Forms/AddEditProductForm";
import FireManager from "../../../../components/Forms/FireManaget";

export const SuperiorShopRoutes =
    <Route key="routes" path="superior" element={<Superior />}>
        <Route path="addProduct" element={<AddEditProductForm mode={'add'} />} />
        <Route path="editProduct" element={<AddEditProductForm mode={'edit'} />} />
        <Route path="fireManager" element={<FireManager />} />
    </Route>
    ;
