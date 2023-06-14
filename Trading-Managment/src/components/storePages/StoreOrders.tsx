import { Typography, Box } from "@mui/material";
import { GridRowId, GridColDef, GridActionsCellItem, DataGrid, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarFilterButton, GridToolbarExport } from "@mui/x-data-grid";
import axios from "axios";
import React, { useEffect } from "react";
import { getProducts } from "../../reducers/productsSlice";
import { getStoresInfo } from "../../reducers/storesSlice";
import { useAppSelector, useAppDispatch } from "../../redux/store";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import Dehaze from '@mui/icons-material/Dehaze';
import Bar2 from "../Bars/Navbar/NavBar2";


const orders = () => {
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];
    const orders = useAppSelector((state) => state.store.storeState.watchedStore.storeOrders).map((order) => {
        return {
            id: order.orderId,
            userId: order.userId,
            num_products: order.productsInStores?.reduce((acc, curr) => acc + curr.quantity, 0),
            price: order.totalPrice,
        }
    });
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds

    const sendPing = () => {
        if (userId != 0) {
            axios.post('http://localhost:4567/api/auth/ping', { userId: userId })
                .then(() => {
                    // Do something with the response if necessary
                })
                .catch(() => {
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

    useEffect(() => {
        const pingInterval = setInterval(sendPing, PING_INTERVAL);

        dispatch(getStoresInfo());
        dispatch(getProducts());
        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
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
    return (
        <>
            <Bar2 headLine={`hello ${privateName} , wellcome to `} />
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
        </>

    )
}
export default orders;
function EditToolbar() {
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