import { Route } from 'react-router-dom';
import Personal from '.';
import EditMyProfileForm from '../../../components/Forms/EditMyProfile';

export const PersonalRoutes = [
    <Route key="routes" path="personal" element={<Personal />} >
        <Route path="editMyProfile" element={<EditMyProfileForm />} />
    </Route>

];