import { Route } from 'react-router-dom';
import Personal from '.';

export const PersonalRoutes = [
    <Route key="routes" path="personal" element={<Personal />} >
        <Route path="changeName" />
    </Route>

];