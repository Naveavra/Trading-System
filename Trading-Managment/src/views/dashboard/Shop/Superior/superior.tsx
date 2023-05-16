import { Outlet, useNavigate, useParams } from "react-router-dom";

import { useEffect, useState } from "react";
import { RootState, useAppDispatch, useAppSelector } from "../../../../redux/store";

import { getClientData, logout } from "../../../../reducers/authSlice";
import Bar2 from "../../../../components/Bars/Navbar/NavBar2";
import { Product } from "../../../../types/systemTypes/Product";
import { products } from "../../../../mock/products";
import { Box, CardContent, Typography, Card } from "@mui/material";
import ProductCard from "../../../../components/ProductCard/Card";
import axios from "axios";
import { getProducts } from "../../../../reducers/productsSlice";
import { getStoresInfo } from "../../../../reducers/storesSlice";
import { Action } from "../../../../types/systemTypes/Action";



const Superior: React.FC = () => {
    const params = useParams();
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const actions = useAppSelector((state) => state);
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.id);
    const products: Product[] = useAppSelector((state) => state.store.storeState.watchedStore.inventory);
    const permmisions = useAppSelector((state: RootState) => state.auth.permmisions).filter((perm) => perm.storeId === storeId);
    const Actions = permmisions.length > 0 ? permmisions[0].actions : [];
    const canRemove = Actions.includes(Action.removeProduct);
    const canEdit = Actions.includes(Action.updateProduct);
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds

    const sendPing = () => {
        console.log("pinging", userId);
        if (userId != 0) {
            console.log("ping", userId);
            axios.post('http://localhost:4567/api/auth/ping', { userId: userId })
                .then(response => {
                    // Do something with the response if necessary
                })
                .catch(error => {
                    // Handle the error if necessary
                });
            // dispatch(ping(userId));
        }
    }
    useEffect(() => {
        console.log("mount");
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        dispatch(getStoresInfo());
        dispatch(getProducts());
        // Stop the ping interval when the user leaves the app
        return () => {
            console.log("unmount");
            clearInterval(pingInterval)
        };
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
            {products.map((product) => {
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
