import { Outlet, useNavigate, useParams } from "react-router-dom";

import { useEffect, useState } from "react";
import { RootState, useAppDispatch, useAppSelector } from "../../../../redux/store";

import { getNotifications, logout } from "../../../../reducers/authSlice";
import Bar2 from "../../../../components/Bars/Navbar/NavBar2";
import { Product } from "../../../../types/systemTypes/Product";
import { products } from "../../../../mock/products";
import { Box, CardContent, Typography, Card, Divider, Button } from "@mui/material";
import ProductCard from "../../../../components/ProductCard/Card";
import axios from "axios";
import { getProducts } from "../../../../reducers/productsSlice";
import { clearStoreError, clearStoresResponse, getStoresInfo } from "../../../../reducers/storesSlice";
import { Action } from "../../../../types/systemTypes/Action";

import { DataGrid, GridActionsCellItem, GridColDef, GridRowId, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExport, GridToolbarFilterButton } from '@mui/x-data-grid';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import Dehaze from '@mui/icons-material/Dehaze';
import React from "react";
import SuccessAlert from "../../../../components/Alerts/success";
import ErrorAlert from "../../../../components/Alerts/error";


const Superior: React.FC = () => {
    const params = useParams();
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const userId = useAppSelector((state) => state.auth.userId);
    const token = useAppSelector((state) => state.auth.token) ?? "";
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const actions = useAppSelector((state) => state);

    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const inventory = useAppSelector((state) => state.store.storeState.watchedStore.inventory);
    const permmisions = useAppSelector((state: RootState) => state.auth.permissions).filter((perm) => perm.storeId === storeId);
    const Actions = permmisions[0]?.actions ?? [];
    const canRemove = Actions.includes(Action.removeProduct);
    const canEdit = Actions.includes(Action.updateProduct);

    const storeError = useAppSelector((state) => state.store.storeState.error);
    const storeMessage = useAppSelector((state) => state.store.storeState.responseData);

    const orders = useAppSelector((state) => state.store.storeState.watchedStore.storeOrders).map((order) => {
        return {
            id: order.orderId,
            userId: order.userId,
            num_products: order.productsInStores?.reduce((acc, curr) => acc + curr.basket?.reduce((acc1, curr1) => acc1 + curr1?.quantity, 0), 0),
            price: order.totalPrice,
        }
    });
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds
    const PING_INTERVAL2 = 5000; // 10 seconds in milliseconds

    const sendPing = () => {
        if (userId != 0) {
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

    const handleEditClick = (id: GridRowId) => () => {
        //todo implement 
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        //todo implement 
    };
    const handleShowInfo = (id: GridRowId) => () => {
        //todo implement 
    };

    const getC = () => {
        if (token) {
            dispatch(getNotifications({ userId: userId, token: token }));
        }
    }

    useEffect(() => {
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        const pingInterval2 = setInterval(getC, PING_INTERVAL2);

        dispatch(getStoresInfo());
        dispatch(getProducts());
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
            clearInterval(pingInterval2)
        };
    }, []);
    const columns: GridColDef[] = React.useMemo(() => {
        return [
            { field: 'id', headerName: 'ID', width: 50, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'userId', headerName: 'userId', width: 150, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'num_products', headerName: 'number of products in order', width: 250, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'price', headerName: 'price', width: 150, editable: false, align: 'center', headerAlign: 'center' },
            {
                field: 'actions',
                type: 'actions',
                headerName: 'Actions',
                width: 130,
                cellClassName: 'actions',
                getActions: ({ id }) => {
                    return [
                        <GridActionsCellItem
                            icon={<DeleteIcon />}
                            label="Delete"
                            onClick={handleDeleteClick(id)}
                            color="inherit"
                        />,
                        <GridActionsCellItem
                            icon={<EditIcon />}
                            label="Edit"
                            onClick={handleEditClick(id)}
                            color="inherit"
                        />,
                        <GridActionsCellItem
                            icon={<Dehaze />}
                            label="data"
                            onClick={handleShowInfo(id)}
                            color="inherit"
                        />,
                    ];
                },
            },
        ];
    }, [handleEditClick, handleDeleteClick, handleShowInfo]);


    return (<>
        <Bar2 headLine={`hellow ${privateName} , wellcome to `} />
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

                </CardContent>
            </Card>
        </Box >
        {storeMessage ? <SuccessAlert message={storeMessage} onClose={() => { dispatch(clearStoresResponse({})) }} /> : null}
        {storeError ? <ErrorAlert message={storeError} onClose={() => { dispatch(clearStoreError({})) }} /> : null}
        <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
            orders
        </Typography >
        <Box sx={{
            height: 550, width: '65%', mt: 7, mb: 2
        }}>
            <DataGrid
                rows={orders}
                columns={columns}
                pagination
                // page={pageState.page - 1}
                // pageSize={pageState.pageSize}
                // paginationMode="server"
                // onPageChange={(newPage) => {
                //     setPageState(old => ({ ...old, page: newPage + 1 }))
                // }}
                // onPageSizeChange={(newPageSize) => setPageState(old => ({ ...old, pageSize: newPageSize }))}

                components={{
                    Toolbar: EditToolbar,
                }}
                sx={{
                    position: 'relative', ml: 20, mb: 3, border: 1, width: '75%', height: '100%', fontSize: 'large'
                }}
            />
        </Box>
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
function EditToolbar() {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const fontSize = 'large';//useAppSelector((state) => state.global.clientSettings.fontSize.size);

    return (
        <div>
            <GridToolbarContainer >
                <GridToolbarDensitySelector sx={{ fontSize: fontSize, mr: 2 }} />
                <GridToolbarFilterButton sx={{ fontSize: fontSize, mr: 2 }} />
                <GridToolbarExport sx={{ fontSize: fontSize, mr: 2 }} />
            </GridToolbarContainer>
        </div>
    );
}


export default Superior;
