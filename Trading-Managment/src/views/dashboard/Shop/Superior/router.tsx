import { Route } from "react-router-dom";
import Superior from "./superior";
import AddEditProductForm from "../../../../components/Forms/AddEditProductForm";
import AppointUser from "../../../../components/Forms/AppointUser";
import FireUser from "../../../../components/Forms/FireUser";
import AddEditStore from "../../../../components/Forms/AddEditStore";
import OpenCloseStore from "../../../../components/Forms/OpenCloseStore";
import UpdatePermissions from "../../../../components/Forms/UpdatePermissions";
import RegularDiscount from "../../../../components/Forms/Discounts/RegularDiscount";
import AddPredicate from "../../../../components/Forms/Discounts/AddPredicate";
import SendComplain from "../../../../components/Forms/SendComplaint";
import AddPurchasePolicy from "../../../../components/Forms/SuppingRoles/AddPurchasePolicy";

export const SuperiorShopRoutes =
    <Route key="routes" path="superior" element={<Superior />}>
        <Route path="addProduct" element={<AddEditProductForm mode={'add'} />} />
        <Route path="editProduct" element={<AddEditProductForm mode={'edit'} />} />
        <Route path="appointManager" element={<AppointUser role={'manager'} />} />
        <Route path="appointOwner" element={<AppointUser role={'owner'} />} />
        <Route path="fireManager" element={<FireUser role={'manager'} />} />
        <Route path="fireOwner" element={<FireUser role={'owner'} />} />
        <Route path="changestoredetails" element={<AddEditStore mode={'edit'} />} />
        <Route path="closeStore" element={<OpenCloseStore />} />
        <Route path="reopenStore" element={<OpenCloseStore />} />
        <Route path="changemanagerpermission" element={<UpdatePermissions />} />
        <Route path="regularDiscount" element={<RegularDiscount tree={false} />} />

        <Route path="regularDiscount/addPredicate" element={<AddPredicate tree={false} />} />
        <Route path="sendComplaint" element={<SendComplain />} />
        <Route path="addpurchaseconstraint" element={<AddPurchasePolicy />} />

    </Route>
    ;
