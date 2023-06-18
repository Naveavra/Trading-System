import { Route } from 'react-router-dom';
import Personal from '.';
import EditMyProfileForm from '../../../components/Forms/EditMyProfile';
import ChangePassword from '../../../components/Forms/ChangePassword';
import SendMsg from '../../../components/Forms/SendMsg';
import SendComplain from '../../../components/Forms/SendComplaint';
import EditBid from '../../../components/Forms/Bids/EditBid';
import BuyCart from '../../../components/Forms/BuyCartForm';

export const PersonalRoutes = [
    <Route key="routes" path="personal" element={<Personal />} >
        <Route path="editMyProfile" element={<EditMyProfileForm />} />
        <Route path="changePassword" element={<ChangePassword />} />
        <Route path="sendMsg" element={<SendMsg />} />
        <Route path=":id/sendComplaint" element={<SendComplain />} />
        <Route path=":storeId/:productId/:bidId/EditBid" element={<EditBid />} />
        <Route path=":bidId/makePurchase" element={<BuyCart personal={true} />} />
    </Route>
];