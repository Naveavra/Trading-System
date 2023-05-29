import { useAppDispatch, useAppSelector } from "../../../redux/store";
import { Card, CardContent, Typography, CardActions, Divider, Box, Grid, TextField, Button } from "@mui/material";
import IconButton from '@mui/material/IconButton';
import EditIcon from '@mui/icons-material/Edit';
import { Outlet, useNavigate } from "react-router-dom";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { LoadingButton } from "@mui/lab";
import { useForm } from "react-hook-form";
import { sendMsgFormValues } from "../../../types/formsTypes";
import { sendMessage } from "../../../reducers/authSlice";
import { useEffect, useMemo, useState } from "react";
import { Dehaze } from "@mui/icons-material";
import { DataGrid, GridActionsCellItem, GridColDef, GridRowId, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExport, GridToolbarFilterButton } from "@mui/x-data-grid";
import DeleteIcon from '@mui/icons-material/DeleteOutlined';

const Admin = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const form = useForm<sendMsgFormValues>();

    const userId = useAppSelector((state) => state.auth.userId);
    const email = useAppSelector((state) => state.auth.userName);
    const age = useAppSelector((state) => state.auth.age);
    const name = email.split('@')[0];
    const birthday = useAppSelector((state) => state.auth.birthday);
    const isLoading = useAppSelector((state) => state.auth.isLoading);
    const [emailError, setEmailError] = useState("");

    const logs = useAppSelector((state) => state.admin.logRecords);

    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        dispatch(sendMessage(form.getValues()));
    }
    const validateEmail = (email: string): void => {
        const emailRegex: RegExp = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        if (!emailRegex.test(email)) {
            form.setError("userName", { type: 'custom', message: 'email isnt valide' }, { shouldFocus: true })
            setEmailError("email isnt valide")
        }
        else {
            setEmailError("");
            form.clearErrors("userName");
        }
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

    //log table
    const columns: GridColDef[] = useMemo(() => {
        return [
            { field: 'id', headerName: 'ID', width: 50, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'userName', headerName: 'user name', width: 130, editable: true, align: 'center', headerAlign: 'center' },

            { field: 'time', headerName: 'time', width: 130, editable: true, align: 'center', headerAlign: 'center' },
            { field: 'content', headerName: 'content', width: 350, editable: true, align: 'center', headerAlign: 'center' },
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
        // dispatch(getCustomers({ synagogue_id: synagogue_id, limit: pageState.pageSize, offset: (pageState.page - 1) * pageState.pageSize }));
    }, [dispatch]);

    return (
        <>
            <Bar3 headLine={"this is your personal data"} />
            <Box sx={{ width: '100%', display: 'flex' }}>
                <Card sx={{ minWidth: 275, width: '30%', mt: 5, ml: 3 }}>
                    <CardContent sx={{ padding: 2 }}>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5" gutterBottom>
                            name : {name}
                        </Typography>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5" component="div">
                            age : {age}
                        </Typography>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                            email : {email}
                        </Typography>
                        <Typography sx={{ fontSize: 20, mt: 2, mb: 2 }} variant="h5">
                            birthday : {birthday}
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <IconButton onClick={() => navigate("editMyProfile")} sx={{ marginLeft: 'auto' }}>
                            <EditIcon />
                        </IconButton>
                    </CardActions>
                </Card>
                <Grid
                    spacing={2}
                    container
                    component="form"
                    onSubmit={handleOnSubmit}
                    sx={{ width: '50%', ml: 10, mt: 5 }}
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
                            sx={{ mt: 3, mb: 2, width: '50%', ml: 23 }}
                            loading={isLoading}
                        >
                            send msg
                        </LoadingButton>
                    </Grid>
                </Grid >
            </Box >
            <Divider />
            <Box sx={{
                height: 550, width: '100%', right: 2, mt: 7
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