import axios from "axios";
import { useMemo } from "react";
import { useEffect } from "react";

import { useAppDispatch, useAppSelector } from "../../redux/store";
import { getComplaints } from "../../reducers/adminSlice";
import { GridRowId, GridColDef, GridActionsCellItem, GridToolbarContainer, GridToolbarDensitySelector, GridToolbarExport, GridToolbarFilterButton, DataGrid } from "@mui/x-data-grid";

import Dehaze from '@mui/icons-material/Dehaze';
import { Typography, Box, Alert, Divider } from "@mui/material";
import { useNavigate } from "react-router-dom";
import Bar2 from "../Bars/Navbar/NavBar2";
import { Action } from "../../types/systemTypes/Action";
import SuccessAlert from "../Alerts/success";
import { clearStoresResponse } from "../../reducers/storesSlice";
import { clearAuthError } from "../../reducers/authSlice";
import ErrorAlert from "../Alerts/error";

const StoreMessages = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const user = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector(state => state.auth.userName);
    const privateName = userName.split('@')[0];

    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const permmisions = useAppSelector((state) => state.auth.permissions)?.filter((perm) => perm.storeId === storeId);
    const Actions = permmisions[0]?.actions ?? [];
    const canAnswer = Actions?.includes(Action.answerMessage);
    const qestions = useAppSelector((state) => state.store.storeState.watchedStore.questions).map((qestion) => {
        return {
            id: qestion.questionId,
            messageId: qestion.messageId,
            userId: qestion.userId,
            content: qestion.content,
            gotFeedback: qestion.gotFeedback,
            seen: qestion.seen,
        }
    });

    const reviews = useAppSelector((state) => state.store.storeState.watchedStore.reviews).map((review) => {
        return {
            id: review.storeReviewId,
            messageId: review.messageId,
            userId: review.userId,
            orderId: review.orderId,
            content: review.content,
            rating: review.rating,
            seen: review.seen,
        }
    });
    //message and error
    const message = useAppSelector((state) => state.store.storeState.responseData);
    const error = useAppSelector((state) => state.store.storeState.error);
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

    const handleShowInfo = (id: GridRowId) => () => {
        navigate(`questions/${id}`);
    };
    useEffect(() => {
        const pingInterval = setInterval(sendPing, PING_INTERVAL);
        dispatch(getComplaints(user));

        // Stop the ping interval when the user leaves the app
        return () => {
            clearInterval(pingInterval)
        };

    }, [dispatch, user]);
    const columns: GridColDef[] = useMemo(() => {
        return [
            { field: 'id', headerName: 'ID', width: 50, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'messageId', headerName: 'message Id', width: 150, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'userId', headerName: 'user Id', width: 150, editable: false, align: 'center', headerAlign: 'center' },

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
                            label="data"
                            onClick={handleShowInfo(id)}
                            color="inherit"
                            disabled={!canAnswer}
                        />,
                    ];
                },
            },
        ];
    }, [handleShowInfo]);

    const reviewsColumns: GridColDef[] = useMemo(() => {
        return [
            { field: 'id', headerName: 'ID', width: 50, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'messageId', headerName: 'message Id', width: 150, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'userId', headerName: 'user Id', width: 150, editable: false, align: 'center', headerAlign: 'center' },

            { field: 'content', headerName: 'content', width: 250, editable: false, align: 'center', headerAlign: 'center' },
            { field: 'rating', headerName: 'rating', width: 150, editable: false, align: 'center', headerAlign: 'center' },
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

        ];
    }, [handleShowInfo]);

    return (
        <>
            <Bar2 headLine={`hello ${privateName} , wellcome to `} />
            <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                questions
            </Typography >
            {message ? <SuccessAlert message={message} onClose={() => { dispatch(clearStoresResponse({})) }} /> : null}
            {error ? <ErrorAlert message={error} onClose={() => { dispatch(clearAuthError()) }} /> : null}
            <Box sx={{
                height: 550, width: '75%', mt: 7, mb: 2
            }}>
                <DataGrid
                    rows={qestions}
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
            <Divider />
            <Typography variant="h4" component="div" sx={{ flexGrow: 1, margin: 'center', ml: 84, mt: 2, alignItems: 'center', justifContent: 'center', fontFamily: 'sans-serif', textDecoration: 'underline' }}>
                reviews
            </Typography >
            <Box sx={{
                height: 550, width: '75%', mt: 7, mb: 2
            }}>
                <DataGrid
                    rows={reviews}
                    columns={reviewsColumns}
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
export default StoreMessages;
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