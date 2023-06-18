import axios from "axios";
import React, { useMemo } from "react";
import { useEffect } from "react";

import { useAppDispatch, useAppSelector } from "../../redux/store";
import { getComplaints } from "../../reducers/adminSlice";
import { GridRowId, GridColDef, GridActionsCellItem, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExport, GridToolbarFilterButton, DataGrid } from "@mui/x-data-grid";
import Bar4 from "../Bars/Navbar/NavBar4"

import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import Dehaze from '@mui/icons-material/Dehaze';
import { Typography, Box, Alert } from "@mui/material";
import { useNavigate } from "react-router-dom";

const Complaints = () => {
    const dispath = useAppDispatch();
    const navigate = useNavigate();
    const user = useAppSelector((state) => state.auth.userId);
    const complaints = useAppSelector((state) => state.admin.complaints).map((complaint) => {
        return {
            id: complaint.complaintId,
            userId: complaint.userId,
            orderId: complaint.orderId,
            content: complaint.content,
            gotFeedback: complaint.gotFeedback,
            seen: complaint.seen,
        }
    });

    const PING_INTERVAL = 10000; // 10 seconds in milliseconds

    const sendPing = () => {
        if (user != 0) {
            axios.post('http://localhost:4567/api/auth/ping', { userId: user })
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
        navigate(`${id}`);
    };

    useEffect(() => {
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        dispath(getComplaints(user));

        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };

    }, [dispath, user]);
    const columns: GridColDef[] = useMemo(() => {
        return [
            { field: 'id', headerName: 'ID', width: 50, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'userId', headerName: 'userId', width: 150, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'orderId', headerName: 'order id', width: 90, editable: false, align: 'center', headerAlign: 'center' },
            { field: 'content', headerName: 'content', width: 250, editable: false, align: 'center', headerAlign: 'center' },
            {
                field: 'gotFeedback', headerName: 'got feedback', width: 150, editable: false, align: 'center', headerAlign: 'center',
                renderCell({ row }) {
                    return (
                        <div>
                            {row.gotFeedback ? <Alert severity="success" sx={{ width: '40%' }}></Alert> : <Alert severity="error" sx={{ width: '40%' }}></Alert>}
                        </div>
                    )
                }
            },
            {
                field: 'seen', headerName: 'seen', width: 150, editable: false, align: 'center', headerAlign: 'center',
                renderCell({ row }) {
                    return (
                        <div>
                            {row.seen ? <Alert severity="error" sx={{ width: '40%' }}></Alert> : <Alert severity="success" sx={{ width: '40%' }}></Alert>}
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
                            icon={<Dehaze />}

                            disabled={complaints.find((complaint) => complaint.id === id)?.gotFeedback}
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
            <Bar4 headLine={"welcome admin"} />
            <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                complaints
            </Typography >
            <Box sx={{
                height: 550, width: '65%', mt: 7, mb: 2
            }}>
                <DataGrid
                    rows={complaints}
                    columns={columns}
                    pagination
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
export default Complaints;
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