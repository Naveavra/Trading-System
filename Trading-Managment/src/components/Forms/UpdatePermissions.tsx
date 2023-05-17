import { useState } from "react";
import { useForm } from "react-hook-form";
import { fireUserFormValues } from "../../types/formsTypes";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, SelectChangeEvent, TextField } from "@mui/material";
import error from "../Alerts/error";
import AlertDialog from "../Dialog/AlertDialog";
import SelectAutoWidth from "../Selectors/AutoWidth";
import { StoreRoleEnum } from "../../types/systemTypes/StoreRole";
import { clearStoreError, fireManager, fireOwner, getStore, patchPermissions } from "../../reducers/storesSlice";
import { patchPermissionsParams } from "../../types/requestTypes/storeTypes";

interface props {
    role: 'owner' | 'manager'
}

const FireUser: React.FC<props> = ({ role }) => {
    const dispatch = useAppDispatch();
    const [open, setOpen] = useState(true);
    const [userToFireId, setUserToFireId] = useState(-1);
    const [user_name, setUser_name] = useState('');
    const userId = useAppSelector((state) => state.auth.userId);
    const form = useForm<patchPermissionsParams>();
    const navigate = useNavigate();
    const params = useParams();
    const isLoading = useAppSelector((state) => state.store.storeState.isLoading);
    const error = useAppSelector((state) => state.store.storeState.error);

    //maybe by id
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.storeId);
    const token = useAppSelector((state) => state.auth.token);

    const managers = useAppSelector((state) => state.store.storeState.watchedStore.storeRoles)?.filter((role) => role.storeRole === StoreRoleEnum.MANAGER);
    const managers_names = managers?.map((role) => role.userName);



    //maybe take it from params
    const handleOnClose = () => {
        setOpen(false);
        navigate(-1);
        dispatch(getStore({ userId: Number(userId), storeId: storeId }));

    }
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        form.setValue('storeId', storeId);
        form.setValue('managerId', userToFireId);
        //split permmision by ',' and put it in form
        const permissions = form.getValues('permissions');
        const ans = permissions[0].split(',');
        form.setValue('permissions', ans);
        const response = dispatch(patchPermissions(form.getValues()));
        response.then((res: { meta: { requestStatus: string; }; }) => {
            if (res.meta.requestStatus === 'fulfilled') {
                handleOnClose();
            }
        });
    }

    const handleChange = (event: SelectChangeEvent) => {
        setUserToFireId(managers.filter((manager) => manager.userName === event.target.value as string)[0].userId);
        setUser_name(event.target.value as string);
    };

    return (
        <>
            <Dialog onClose={handleOnClose} open={open}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 300,
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
                                <p>choose the user u want to update his permmisions</p>
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <SelectAutoWidth label={'managers in the store'} values={managers_names} labels={managers_names} value={user_name} handleChange={handleChange} />
                        </Grid>

                        <Grid item xs={12}>
                            <TextField
                                name="permissions"
                                type="text"
                                fullWidth
                                label="permission list"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('permissions', {
                                        required: {
                                            value: true,
                                            message: "permissions are required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['permissions'] ?? false}
                                helperText={form.formState.errors['permissions']?.message ?? undefined}
                            />
                        </Grid>
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
export default FireUser;