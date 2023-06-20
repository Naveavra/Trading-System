import { SyntheticEvent, useCallback, useState } from "react";
import { useForm } from "react-hook-form";
import { fireUserFormValues } from "../../types/formsTypes";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, SelectChangeEvent, TextField, Checkbox, FormControlLabel } from "@mui/material";
import error from "../Alerts/error";
import AlertDialog from "../Dialog/AlertDialog";
import SelectAutoWidth from "../Selectors/AutoWidth";
import { StoreRoleEnum } from "../../types/systemTypes/StoreRole";
import { clearStoreError, fireManager, fireOwner, getStore, patchPermissions } from "../../reducers/storesSlice";
import { patchPermissionsParams } from "../../types/requestTypes/storeTypes";
import { Action, ManagerActions } from "../../types/systemTypes/Action";



const UpdatePermissions = () => {
    const dispatch = useAppDispatch();
    const [userToFireId, setUserToFireId] = useState(-1);
    const [user_name, setUser_name] = useState('');

    const userId = useAppSelector((state) => state.auth.userId);
    const form = useForm<patchPermissionsParams>();
    const navigate = useNavigate();

    const isLoading = useAppSelector((state) => state.store.storeState.isLoading);
    const error = useAppSelector((state) => state.store.storeState.error);

    //maybe by id
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const token = useAppSelector((state) => state.auth.token);

    const managers = useAppSelector((state) => state.store.storeState.watchedStore.roles)?.filter((role) => role.storeRole === StoreRoleEnum.MANAGER);
    const managers_names = managers?.map((role) => role.userName) ?? [];

    const selectedManagerPermissions = managers?.filter((manager) => manager.userName === user_name)[0]?.actions ?? [];
    const AllActionsButSelected = Object.values(Action).filter((action) => !selectedManagerPermissions.includes(action) && ManagerActions.includes(action));

    const [selectedActions, setSelectedActions] = useState<string[]>([]);
    const [mode, setMode] = useState<string>('add');
    //maybe take it from params
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior');
        dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        form.setValue('storeId', storeId);
        form.setValue('managerId', userToFireId);
        form.setValue('mode', mode);
        form.setValue('permissions', selectedActions);

        console.log(form.getValues());
        dispatch(patchPermissions(form.getValues()));
        handleOnClose();
    }

    const handleChange = (event: SelectChangeEvent) => {
        setUserToFireId(managers.filter((manager) => manager.userName === event.target.value as string)[0].userId);
        setUser_name(event.target.value as string);
    };

    const handleCheckBox = (event: SyntheticEvent, action1: string) => {
        if ((event.target as HTMLInputElement).checked) {
            setSelectedActions([...selectedActions, action1])
        }
        else {
            setSelectedActions(selectedActions?.filter((action) => action != action1));
        }
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 750,
                        width: '80%',
                        flexDirection: 'column',
                        alignItems: 'center',
                        marginLeft: 'auto',
                        marginRight: 'auto',
                        marginBottom: -2,
                        bgcolor: 'background.paper',

                    }}
                >
                    <Grid
                        spacing={2}
                        container
                        component="form"
                        onSubmit={handleOnSubmit}
                    >

                        <Grid item xs={12}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                <p>choose the method u want to do , add or remove actions</p>
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <SelectAutoWidth label={'mode'} values={['add', 'remove']} labels={['add', 'remove']} value={mode} handleChange={(e) => { setMode(e.target.value as string) }} />
                        </Grid>
                        <Grid item xs={12}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                <p>choose the manager u want to {mode === 'add' ? 'add to him some permissions' : 'remove some of his permmisions'} </p>
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <SelectAutoWidth label={'managers in the store'} values={managers_names} labels={managers_names} value={user_name} handleChange={handleChange} />
                        </Grid>
                        <Grid item xs={12}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                <p>choose actions to {mode === 'add' ? 'add' : 'remove'} {mode === 'add' ? 'to' : 'from'} {user_name}</p>
                            </Typography>
                        </Grid>
                        {user_name != '' ?
                            <>
                                {mode === 'add' ?
                                    AllActionsButSelected.map((action, index) => {
                                        return (
                                            <Grid item xs={12} key={index}>
                                                <FormControlLabel control={<Checkbox />} label={action} onChange={(e) => { handleCheckBox(e, action) }} />
                                            </Grid>
                                        )
                                    })
                                    :
                                    selectedManagerPermissions.map((action, index) => {
                                        return (
                                            <Grid item xs={12} key={index}>
                                                <FormControlLabel control={<Checkbox />} label={action} onChange={(e) => { handleCheckBox(e, action) }} />
                                            </Grid>
                                        )
                                    })
                                }
                            </> :
                            null
                        }
                        <Grid item xs={12}>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                update
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearStoreError({})); }} text={error} sevirity={"error"} />
                : null}
        </>
    );

}
export default UpdatePermissions;