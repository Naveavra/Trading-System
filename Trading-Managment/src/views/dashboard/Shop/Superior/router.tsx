import { Route } from "react-router-dom";
import Superior from "./superior";
import AddEditProductForm from "../../../../components/Forms/AddEditProductForm";
import AppointUser from "../../../../components/Forms/AppointUser";
import FireUser from "../../../../components/Forms/FireUser";

export const SuperiorShopRoutes =
    <Route key="routes" path="superior" element={<Superior />}>
        <Route path="addProduct" element={<AddEditProductForm mode={'add'} />} />
        <Route path="updateProduct" element={<AddEditProductForm mode={'edit'} />} />
        <Route path="appointManager" element={<AppointUser role={'manager'} />} />
        <Route path="appointOwner" element={<AppointUser role={'owner'} />} />
        <Route path="fireManager" element={<FireUser role={'manager'} />} />
        <Route path="fireOwner" element={<FireUser role={'owner'} />} />

    </Route>
    ;
