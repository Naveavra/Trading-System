import { useAppDispatch, useAppSelector } from "../../../redux/store";
import { Card, CardContent, Typography, CardActions, Divider, Box, Alert } from "@mui/material";
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import { Outlet, useNavigate } from "react-router-dom";
import { useEffect, useMemo } from "react";
import { Dehaze } from "@mui/icons-material";
import { DataGrid, GridActionsCellItem, GridColDef, GridRowId, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExport, GridToolbarFilterButton } from "@mui/x-data-grid";
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import Bar4 from "../../../components/Bars/Navbar/NavBar4";
import { adminResign, clearAdminError, clearAdminMsg, getComplaints, getLogger, getMarketStatus } from "../../../reducers/adminSlice";
import PasswordIcon from '@mui/icons-material/Password';
import { getClientData, getNotifications, resetAuth, setTrue } from "../../../reducers/authSlice";
import CancelIcon from '@mui/icons-material/Cancel';
import { reset } from "../../../reducers/discountSlice";
import ErrorAlert from "../../../components/Alerts/error";
import SuccessAlert from "../../../components/Alerts/success";
import { getStore, getStoresInfo } from "../../../reducers/storesSlice";

const Admin = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();


    const userId = useAppSelector((state) => state.auth.userId);
    const token = useAppSelector((state) => state.auth.token) ?? "";
    const userName = useAppSelector((state) => state.auth.userName);
    const name = userName.split('@')[0];
    const msg = useAppSelector((state) => state.admin.msg);
    const error = useAppSelector((state) => state.admin.error);
    const first = useAppSelector((state) => state.auth.first);

    const logs = useAppSelector((state) => state.admin.logRecords) ?? [{ userName: "", id: 0, content: "", status: "" }];
    const systemStatus = useAppSelector((state) => state.admin.status);
    //works but does not look good


    const handleResign = () => {
        dispatch(adminResign(userId)).then((res) => {
            if (res.payload?.message?.data !== 'the admin cannot be removed because it is the only admin in the system') {
                dispatch(resetAuth());
                navigate("/auth/login");
            }
        }).catch((err) => {
            console.log(err);
        });
    }



    const handleEditClick = (id: GridRowId) => () => {
        //navigate(`/dashboard/customers/edit/${id}`);
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        //     dispatch(deleteCustomer(id as number));
        //     dispatch(getCustomers({ synagogue_id: synagogue_id, limit: pageState.pageSize, offset: (pageState.page - 1) * pageState.pageSize }));
    };
    const handleShowInfo = (id: GridRowId) => () => {
        // dispatch(setWhatchedCustomer(id as number));
        // navigate(`/dashboard/customers/${id}`);
    };
    interface NumberToVoidFunctionMap {
        [key: number]: () => void;
    }
    const hashMap: NumberToVoidFunctionMap = {
        0: () => {
            dispatch(getClientData({ userId: userId }));
            fetchNotification();
        },
        3: () => {
            dispatch(getComplaints(userId));
            fetchNotification();
        },
        6: () => {
            dispatch(getClientData({ userId: userId }));
            dispatch(getComplaints(userId));
            fetchNotification();
        },
    };
    const fetchNotification = async () => {
        try {
            if (token != "" && userName != 'guest') {
                const response = await dispatch(getNotifications({ userId: userId, token: token }));
                if (response.payload != null) {
                    hashMap[response.payload?.opcode]();
                }
            }
        } catch (error) {
            console.error('Error fetching notification:', error);
        }
    };
    useEffect(() => {
        dispatch(getLogger(userId));
        dispatch(getStoresInfo());
        //---------------------notifications---------------------
        if (!first) {
            fetchNotification();
            dispatch(setTrue());
        }
    }, [userId, dispatch])

    //log table
    const columns: GridColDef[] = useMemo(() => {
        return [
            { field: 'id', headerName: 'ID', width: 50, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'userName', headerName: 'user name', width: 330, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'time', headerName: 'time', width: 130, editable: false, align: 'center', headerAlign: 'center' },
            { field: 'content', headerName: 'content', width: 350, editable: false, align: 'center', headerAlign: 'center' },
            {
                field: 'status',
                headerName: 'status',
                width: 80,
                renderCell({ row }) {
                    return (
                        <div>
                            {row.status === 'Fail' ? <Alert severity="error" sx={{ width: '40%' }}></Alert> : <Alert severity="success" sx={{ width: '40%' }}></Alert>}
                        </div>
                    )
                }
            },
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


    useEffect(() => {
        dispatch(getLogger(userId));
        dispatch(getMarketStatus(userId));
    }, [dispatch]);

    return (
        <>
            <Bar4 headLine={"welcome admin"} />
            {msg ? <SuccessAlert message={msg} onClose={() => { dispatch(clearAdminMsg()) }} /> : null}
            {error ? <ErrorAlert message={error} onClose={() => { dispatch(clearAdminError()) }} /> : null}
            <Box sx={{ width: '100%', display: 'flex' }}>
                <Box sx={{ width: '80%' }}>
                    <Box sx={{ width: '100%', display: 'flex' }}>
                        <Typography sx={{ fontSize: 25, mt: 3, ml: '13%' }} gutterBottom>
                            your personal data
                        </Typography>
                    </Box>
                    <Box sx={{ width: '100%', display: 'flex' }}>
                        <Card sx={{ minWidth: 275, width: '50%', mt: 5, ml: 3 }}>
                            <CardContent sx={{ padding: 2 }}>
                                <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5" gutterBottom>
                                    name : {name}
                                </Typography>
                                <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                                    email : {userName}
                                </Typography>
                            </CardContent>
                            <CardActions sx={{ marginTop: 10 }}>
                                <IconButton onClick={handleResign} sx={{ marginLeft: 'auto' }}>
                                    <CancelIcon />
                                </IconButton>
                                <IconButton onClick={() => navigate("editMyProfile")} sx={{ marginLeft: 'auto' }}>
                                    <EditIcon />
                                </IconButton>
                                <IconButton onClick={() => navigate("changePassword")} sx={{ marginLeft: 'auto' }}>
                                    <PasswordIcon />
                                </IconButton>
                            </CardActions>
                        </Card>
                    </Box >
                </Box>
                <Box sx={{ width: '70%' }}>
                    <Box sx={{ width: '100%', display: 'flex' }}>
                        <Typography sx={{ fontSize: 25, mt: 3, ml: '33%' }} gutterBottom>
                            system state
                        </Typography>
                    </Box>
                    <Box sx={{ width: '100%', display: 'flex' }}>
                        <Card sx={{ minWidth: 275, width: '80%', mt: 4, ml: 3 }}>
                            <CardContent sx={{ padding: 2 }}>
                                <Box display={'flex'}>
                                    <Box sx={{ width: '85%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2, mr: 2 }} variant="h5" gutterBottom>
                                            average purchase : {systemStatus.averagePurchase}
                                        </Typography>
                                    </Box>
                                    <Box sx={{ width: '50%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                                            member count : {systemStatus.memberCount}
                                        </Typography>
                                    </Box>
                                </Box>
                                <Box display={'flex'}>
                                    <Box sx={{ width: '85%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2, mr: 2 }} variant="h5">
                                            average user in system : {systemStatus.averageUserIn}
                                        </Typography>
                                    </Box>
                                    <Box sx={{ width: '50%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                                            guest count : {systemStatus.guestCount}
                                        </Typography>
                                    </Box>
                                </Box>
                                <Box display={'flex'}>
                                    <Box sx={{ width: '85%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2, mr: 2 }} variant="h5">
                                            average registered : {systemStatus.averageRegistered}
                                        </Typography>
                                    </Box>
                                    <Box sx={{ width: '50%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                                            register count : {systemStatus.registeredCount}
                                        </Typography>
                                    </Box>
                                </Box>
                                <Box display={'flex'}>
                                    <Box sx={{ width: '85%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2, mr: 2 }} variant="h5">
                                            average user out : {systemStatus.averageUserOut}
                                        </Typography>
                                    </Box>
                                    <Box sx={{ width: '50%' }}>
                                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                                            purchase count : {systemStatus.purchaseCount}
                                        </Typography>
                                    </Box>
                                </Box>
                            </CardContent>
                        </Card>
                    </Box >
                </Box>
            </Box>

            <Divider />
            <Typography sx={{ fontSize: 25, mt: 3, ml: '40%' }} gutterBottom>
                system log history
            </Typography>
            <Box sx={{
                height: 550, width: '90%', right: 2, mt: 7, ml: '10%', borderColor: 'divider'
            }}>
                <DataGrid
                    rows={logs}
                    columns={columns}
                    components={{
                        Toolbar: EditToolbar,
                    }}
                    sx={{
                        position: 'relative', ml: 20, mb: 3, border: 1, width: '75%', height: '100%', fontSize: 'large'
                    }}
                />
            </Box>
            <Outlet />
        </>
    );
};
function EditToolbar() {
    return (
        <div>
            <GridToolbarContainer >
                <GridToolbarDensitySelector sx={{ fontSize: 'medium', mr: 2 }} />
                <GridToolbarFilterButton sx={{ fontSize: 'medium', mr: 2 }} />
                <GridToolbarExport sx={{ fontSize: 'medium', mr: 2 }} />
            </GridToolbarContainer>
        </div>
    );
}

export default Admin;
