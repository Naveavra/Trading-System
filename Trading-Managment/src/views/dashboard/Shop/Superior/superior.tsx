import { Outlet, useNavigate, useParams } from "react-router-dom";

import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../../../redux/store";

import { getClientData, logout } from "../../../../reducers/authSlice";
import Bar2 from "../../../../components/Bars/Navbar/NavBar2";
import { Product } from "../../../../types/systemTypes/Product";
import { products } from "../../../../mock/products";
import { Box, CardContent, Typography, Card } from "@mui/material";
import ProductCard from "../../../../components/ProductCard/Card";



const Superior: React.FC = () => {
    const params = useParams();
    const userId = params.id ?? '-1';
    const token = 'token';//params.token ?? 'token';
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    //console.log("params", params);
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const actions = useAppSelector((state) => state);
    //const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    const prodiucts: Product[] = products;//useAppSelector((state) => state.store.storeState.watchedStore.inventory);
    const canRemove = true;// useAppSelector((state) => state.auth.permmisions).filter((perm) => perm.storeId === storeId)[0].actions.includes(Action.removeProduct);
    const canEdit = true;// useAppSelector((state) => state.auth.permmisions).filter((perm) => perm.storeId === storeId)[0].actions.includes(Action.updateProduct);
    useEffect(() => {
        // dispatch(getClientData({ userId: parseInt(userId), token: token }));
    }, []);

    return (<>
        <Bar2 headLine={`wellcome to the store ${privateName}`} />
        <Box>
            <Card sx={{ minWidth: 275 }}>
                <CardContent>
                    <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                        about us
                    </Typography >
                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>

                    </Typography>
                    <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                        <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 73, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                            {store.description}
                            decreaption about the store
                        </Typography >
                    </Box>

                </CardContent>
            </Card>
        </Box >
        <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
            {prodiucts.map((product) => {
                return (
                    <ProductCard item={product} canDelete={canRemove} canEdit={canEdit} key={product.id} />
                );
            })
            }
        </Box>
        <Outlet />
    </>
    );
}


export default Superior;
