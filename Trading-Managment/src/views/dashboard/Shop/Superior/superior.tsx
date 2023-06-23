import { Outlet, useNavigate, useParams } from "react-router-dom";

import { useEffect, useState } from "react";
import { RootState, useAppDispatch, useAppSelector } from "../../../../redux/store";

import { getClientData, getNotifications, resetAuth } from "../../../../reducers/authSlice";
import Bar2 from "../../../../components/Bars/Navbar/NavBar2";
import { Box, CardContent, Typography, Card, Divider } from "@mui/material";
import ProductCard from "../../../../components/ProductInStore/Card";
import axios from "axios";
import { clearProductError, clearProductMsg, clearProductsError, getProducts } from "../../../../reducers/productsSlice";
import { clearStoreError, clearStoresResponse, getStore, getStoresInfo } from "../../../../reducers/storesSlice";
import { Action } from "../../../../types/systemTypes/Action";

import React from "react";
import SuccessAlert from "../../../../components/Alerts/success";
import ErrorAlert from "../../../../components/Alerts/error";
import { clearBidError, clearBidMsg } from "../../../../reducers/bidSlice";
import { clearDiscountError, clearDiscountMsg } from "../../../../reducers/discountSlice";
import { clearShopRuleError, clearShopRuleMessage } from "../../../../reducers/ShoppingRules";
import { getComplaints, removeUser } from "../../../../reducers/adminSlice";
import { getCart } from "../../../../reducers/cartSlice";


const Superior: React.FC = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const token = useAppSelector((state) => state.auth.token);
    const userId = useAppSelector((state) => state.auth.userId);
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const actions = useAppSelector((state) => state);
    const [left, setLeft] = useState(false);

    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const inventory = useAppSelector((state) => state.store.storeState.watchedStore.inventory);
    const permmisions = useAppSelector((state: RootState) => state.auth.permissions)?.filter((perm) => perm.storeId === storeId);
    const Actions = permmisions[0]?.actions ?? [];
    const canRemove = Actions?.includes(Action.removeProduct);
    const canEdit = Actions?.includes(Action.updateProduct);

    const storeError = useAppSelector((state) => state.store.storeState.error);
    const storeMessage = useAppSelector((state) => state.store.storeState.responseData);

    const bidMessage = useAppSelector((state) => state.bid.message);
    const bidError = useAppSelector((state) => state.bid.error);

    const discountMessage = useAppSelector((state) => state.discount.discountState.responseData);
    const discountError = useAppSelector((state) => state.discount.discountState.error);

    const productMessage = useAppSelector((state) => state.product.productState.responseData);
    const productError = useAppSelector((state) => state.product.productState.error);

    const shopRuleMessage = useAppSelector((state) => state.shoppingRule.message);
    const shopRuleError = useAppSelector((state) => state.shoppingRule.error);

    interface NumberToVoidFunctionMap {
        [key: number]: () => void;
    }

    const hashMap: NumberToVoidFunctionMap = {
        0: () => {
            debugger;
            dispatch(getClientData({ userId: userId }));
            fetchNotification();
        },
        1: () => {
            debugger;
            dispatch(getStore({ userId: userId, storeId: storeId }));
            fetchNotification();
        },
        2: () => {
            // handleAdmin();
            fetchNotification();
        },
        3: () => {
            dispatch(getComplaints(userId));
            fetchNotification();
        },
        4: () => {
            setLeft(true);
            dispatch(removeUser(userName));
            dispatch(resetAuth());
            navigate('/auth/login');
        },
        5: () => {
            debugger;
            dispatch(getClientData({ userId: userId }));
            dispatch(getStore({ userId: userId, storeId: storeId }));
            fetchNotification();
        },
        6: () => {
            dispatch(getClientData({ userId: userId }));
            dispatch(getComplaints(userId));
            fetchNotification();
        },
        7: () => {
            dispatch(getStore({ userId: userId, storeId: storeId }));
            dispatch(getComplaints(userId));
            fetchNotification();
        },
    };
    const fetchNotification = async () => {
        try {
            if (token != "" && userName != 'guest' && !left) {
                const response = await dispatch(getNotifications({ userId: userId, token: token }));
                debugger;
                if (response.payload != null) {
                    hashMap[response.payload?.opcode ?? 0]();
                }
            }
        } catch (error) {
            console.error('Error fetching notification:', error);
        }
    };
    useEffect(() => {

        dispatch(getProducts());
        dispatch(getCart({ userId: userId }));
        // Stop the ping interval when the user leaves the app
        //---------------------notifications---------------------

        fetchNotification();
    }, [userId, dispatch, token, userName, storeId])

    return (<>
        <Bar2 headLine={`hello ${privateName} , wellcome to `} />
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
                        </Typography >
                    </Box>
                    <Box sx={{ flexGrow: 1, flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                        <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                            store discounts:
                        </Typography >
                        {store.discounts?.map((discount, index) => {
                            return (
                                <Typography key={index} variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 10, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                    {discount.content}
                                </Typography >
                            )
                        })}
                    </Box>
                    <Box sx={{ flexGrow: 1, flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
                        <Typography variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                            store shopping rules:
                        </Typography >
                        {store.purchasePolicies?.map((shopRule, index) => {
                            return (
                                <Typography key={index} variant="h6" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 10, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif' }}>
                                    {shopRule.content}
                                </Typography >
                            )
                        })}
                    </Box>
                </CardContent>
            </Card>
        </Box >
        {storeMessage ? <SuccessAlert message={storeMessage} onClose={() => { dispatch(clearStoresResponse({})) }} /> : null}
        {storeError ? <ErrorAlert message={storeError} onClose={() => { dispatch(clearStoreError({})) }} /> : null}
        {bidMessage ? <SuccessAlert message={bidMessage} onClose={() => { dispatch(clearBidMsg()) }} /> : null}
        {bidError ? <ErrorAlert message={bidError} onClose={() => { dispatch(clearBidError()) }} /> : null}
        {discountMessage ? <SuccessAlert message={discountMessage} onClose={() => { dispatch(clearDiscountMsg()) }} /> : null}
        {discountError ? <ErrorAlert message={discountError} onClose={() => { dispatch(clearDiscountError()) }} /> : null}
        {productMessage ? <SuccessAlert message={productMessage} onClose={() => { dispatch(clearProductMsg()) }} /> : null}
        {productError ? <ErrorAlert message={productError} onClose={() => {
            dispatch(clearProductError());
            dispatch(clearProductsError());
        }} /> : null}
        {shopRuleMessage ? <SuccessAlert message={shopRuleMessage} onClose={() => { dispatch(clearShopRuleMessage()) }} /> : null}
        {shopRuleError ? <ErrorAlert message={shopRuleError} onClose={() => { dispatch(clearShopRuleError()) }} /> : null}

        <Divider />
        <Box sx={{ flexGrow: 1, display: 'flex', flexWrap: 'wrap', flexBasis: 4, gap: '16px' }} >
            {inventory.map((product) => {
                return (
                    <ProductCard item={product} canDelete={canRemove} canEdit={canEdit} key={product.productId} />
                );
            })
            }
        </Box>

        <Outlet />
    </>
    );
}



export default Superior;
