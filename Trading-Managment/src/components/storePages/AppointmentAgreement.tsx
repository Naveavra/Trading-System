import { Box, Typography, Card, CardContent, Alert, CardActions, IconButton } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import Bar3 from "../Bars/Navbar/NavBar3";
import { useEffect } from "react";
import { answerOnWaitingAppointment, clearStoreError, clearStoresResponse, getStore } from "../../reducers/storesSlice";
import ThumbDownOffAltIcon from '@mui/icons-material/ThumbDownOffAlt';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { StoreRoleEnum } from "../../types/systemTypes/StoreRole";
import { Action } from "../../types/systemTypes/Action";
import ErrorAlert from "../Alerts/error";
import SuccessAlert from "../Alerts/success";
const AppointmentAgreement = () => {
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const permissions = useAppSelector((state) => state.auth.permissions);
    const actions = permissions?.filter((perm) => perm.storeId == store.storeId)[0]?.actions ?? [];
    const navigate = useNavigate();
    const appointments = useAppSelector((state) => state.store.storeState.watchedStore.appointments);
    const userRole = useAppSelector((state) => state.auth.storeRoles).filter((role) => role.storeId == store.storeId)[0]?.storeRole
    const storeError = useAppSelector((state) => state.store.storeState.error);
    const storeMessage = useAppSelector((state) => state.store.storeState.responseData);

    useEffect(() => {
        dispatch(getStore({ userId: userId, storeId: store.storeId }));
    }, []);


    return (
        <>
            <Bar3 headLine={"wellcome to waiting appointemts center"} />
            <Typography variant="h6" component="div" sx={{ display: 'flex', justifyContent: 'center', flexGrow: 2 }}>
                the is the waiting appointemts in the store
            </Typography>
            {storeMessage ? <SuccessAlert message={storeMessage} onClose={() => { dispatch(clearStoresResponse({})) }} /> : null}
            {storeError ? <ErrorAlert message={storeError} onClose={() => { dispatch(clearStoreError({})) }} /> : null}
            <Box sx={{ display: "flex", width: '100%', mb: 2 }}>
                {
                    appointments?.map((appointment, index) => {
                        return (
                            <Card sx={{ width: 200, mt: 5, ml: 3 }} key={index}>
                                <CardContent>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        store id : {appointment.storeId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        memane: {appointment.memane}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        memune: {appointment.memune}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        role : {appointment.role}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        approvers :
                                    </Typography>
                                    {appointment.approved?.map((approver, index) => {
                                        return (
                                            <Typography sx={{ fontSize: 14, ml: 5 }} color="text.secondary" gutterBottom key={index}>
                                                {approver}
                                            </Typography>
                                        )
                                    })}
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        need to approve :
                                    </Typography>
                                    {appointment.notApproved?.map((approver, index) => {
                                        return (
                                            <Typography sx={{ fontSize: 14, ml: 5 }} color="text.secondary" gutterBottom key={index}>
                                                {approver}
                                            </Typography>
                                        )
                                    })}
                                    {appointment.status ?
                                        <Alert severity="success">Completed</Alert>
                                        :
                                        <Alert severity="info">pending</Alert>
                                    }
                                </CardContent>
                                <CardActions>
                                    {!appointment.status && appointment.notApproved.includes(userName) && ((appointment.role === StoreRoleEnum.MANAGER && actions.includes(Action.appointManager)) || (appointment.role === StoreRoleEnum.OWNER && actions.includes(Action.appointOwner))) ?
                                        <>
                                            <IconButton onClick={() => {
                                                dispatch(answerOnWaitingAppointment({ userId: userId, storeId: store.storeId, fatherName: appointment.memane, childName: appointment.memune, answer: true })).then(() => {
                                                    dispatch(getStore({ userId: userId, storeId: store.storeId }));
                                                })
                                            }}>
                                                <ThumbUpIcon />
                                            </IconButton>
                                            <IconButton onClick={() => {
                                                dispatch(answerOnWaitingAppointment({ userId: userId, storeId: store.storeId, fatherName: appointment.memane, childName: appointment.memune, answer: false })).then(() => {
                                                    dispatch(getStore({ userId: userId, storeId: store.storeId }));
                                                })
                                            }}>
                                                <ThumbDownOffAltIcon />
                                            </IconButton>
                                        </>
                                        : null}
                                </CardActions>
                            </Card>
                        )
                    })
                }
            </Box >
        </>
    )
}
export default AppointmentAgreement;