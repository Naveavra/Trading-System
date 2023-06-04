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
import { getLogger } from "../../../reducers/adminSlice";
import PasswordIcon from '@mui/icons-material/Password';


const Admin = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();


    const userId = useAppSelector((state) => state.auth.userId);
    const email = useAppSelector((state) => state.auth.userName);
    const name = email.split('@')[0];
    const isLoading = useAppSelector((state) => state.auth.isLoading);

    const logs = useAppSelector((state) => state.admin.logRecords) ?? [{ userName: "", id: 0, content: "", status: "" }];
    //works but does not look good
    const arrayForSort = [...logs]
    const max = arrayForSort.length > 0 ?
        arrayForSort.sort((a, b) => { if (a.content.length > b.content.length) { return a.content.length } else { return b.content.length } })[0].content.length :
        330;



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


    //log table
    const columns: GridColDef[] = useMemo(() => {
        return [
            { field: 'id', headerName: 'ID', width: 50, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'userName', headerName: 'user name', width: 330, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'time', headerName: 'time', width: 130, editable: false, align: 'center', headerAlign: 'center' },
            { field: 'content', headerName: 'content', width: 200, editable: false, align: 'center', headerAlign: 'center' },
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
        dispatch(getLogger(userId))
    }, [dispatch]);

    return (
        <>
            <Bar4 headLine={"welcome admin"} />
            <Box sx={{ width: '100%', display: 'flex' }}>
                <Typography sx={{ fontSize: 25, mt: 3, ml: '10%' }} gutterBottom>
                    your personal data
                </Typography>
            </Box>
            <Box sx={{ width: '100%', display: 'flex' }}>
                <Card sx={{ minWidth: 275, width: '30%', mt: 5, ml: 3 }}>
                    <CardContent sx={{ padding: 2 }}>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5" gutterBottom>
                            name : {name}
                        </Typography>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                            email : {email}
                        </Typography>
                    </CardContent>
                    <CardActions sx={{ marginTop: 10 }}>
                        <IconButton onClick={() => navigate("editMyProfile")} sx={{ marginLeft: 'auto' }}>
                            <EditIcon />
                        </IconButton>
                        <IconButton onClick={() => navigate("changePassword")} sx={{ marginLeft: 'auto' }}>
                            <PasswordIcon />
                        </IconButton>
                    </CardActions>
                </Card>
            </Box >

            <Divider />
            <Typography sx={{ fontSize: 25, mt: 3, ml: '40%' }} gutterBottom>
                system log history
            </Typography>
            <Box sx={{
                height: 550, width: '70%', right: 2, mt: 7, ml: '10%', borderColor: 'divider'
            }}>
                <DataGrid
                    rows={logs}
                    columns={columns}

                    pagination

                    paginationMode="server"


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
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
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
{/* <Grid
                    spacing={2}
                    container
                    component="form"
                    onSubmit={handleOnSubmit}
                    sx={{ width: '30%', ml: 2, mt: 5 }}
                >
                    <Grid item xs={12}>
                        <TextField
                            name="email"
                            type="text"
                            fullWidth
                            label="name"
                            sx={{ mt: 1, mb: 1 }}
                            inputProps={{
                                ...form.register('email', {
                                    required: {
                                        value: true,
                                        message: "name is required"
                                    }
                                })
                            }}
                            error={!!form.formState.errors['email'] ?? false}
                            helperText={form.formState.errors['email']?.message ?? undefined}
                            onChange={(e) => {
                                validateEmail(e.target.value)
                            }}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            name="password"
                            type="password"
                            fullWidth
                            label="password"
                            sx={{ mt: 1, mb: 1 }}
                            inputProps={{
                                ...form.register('password', {
                                    required: {
                                        value: true,
                                        message: "msg is mendatory"
                                    }
                                })
                            }}
                            error={!!form.formState.errors['password'] ?? false}
                            helperText={form.formState.errors['password']?.message ?? undefined}
                            onChange={(e) => {
                                validatePassword(e.target.value)
                            }}
                        />
                    </Grid>

                    <Grid item xs={12}>
                        <LoadingButton
                            disabled={newPasswordError != ""}
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, width: '50%', ml: 19 }}
                            loading={isLoading}
                        >
                            add admin
                        </LoadingButton>
                    </Grid>
                </Grid >
                <Grid
                    spacing={2}
                    container
                    component="form"
                    onSubmit={handleOnSubmit}
                    sx={{ width: '30%', ml: 10, mt: 5 }}
                >
                    <Grid item xs={12}>
                        <TextField
                            name="email"
                            type="text"
                            fullWidth
                            label="name"
                            sx={{ mt: 1, mb: 1 }}
                            inputProps={{
                                ...form.register('userName', {
                                    required: {
                                        value: true,
                                        message: "name is required"
                                    }
                                })
                            }}
                            error={!!form.formState.errors['userName'] ?? false}
                            helperText={form.formState.errors['userName']?.message ?? undefined}
                            onChange={(e) => {
                                validateEmail(e.target.value)
                            }}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            name="message"
                            type="text"
                            fullWidth
                            label="message"
                            sx={{ mt: 1, mb: 1 }}
                            inputProps={{
                                ...form.register('message', {
                                    required: {
                                        value: true,
                                        message: "msg is mendatory"
                                    }
                                })
                            }}
                            error={!!form.formState.errors['message'] ?? false}
                            helperText={form.formState.errors['message']?.message ?? undefined}
                        />
                    </Grid>

                    <Grid item xs={12}>
                        <LoadingButton
                            disabled={emailError != ""}
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, width: '50%', ml: 19 }}
                            loading={isLoading}
                        >
                            send msg
                        </LoadingButton>
                    </Grid>
                </Grid > */}