import { useCallback, useState } from "react";
import { useForm } from "react-hook-form";
import { fireUserFormValues } from "../../types/formsTypes";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, SelectChangeEvent } from "@mui/material";
import AlertDialog from "../Dialog/AlertDialog";
import SelectAutoWidth from "../Selectors/AutoWidth";
import { StoreRoleEnum } from "../../types/systemTypes/StoreRole";
import { clearStoreError, fireManager, fireOwner, getStore } from "../../reducers/storesSlice";

interface props {
    role: 'owner' | 'manager'
}

const FireUser: React.FC<props> = ({ role }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const [userToFireId, setUserToFireId] = useState(-1);
    const form = useForm<fireUserFormValues>();


    const token = useAppSelector((state) => state.auth.token);
    const userId = useAppSelector((state) => state.auth.userId)
    const isLoading = useAppSelector((state) => state.store.storeState.isLoading);
    const error = useAppSelector((state) => state.store.storeState.error);

    //maybe by id

    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const storeId = store.storeId;

    const managers = store.roles?.filter((role) => role.storeRole === StoreRoleEnum.MANAGER);
    const managers_names = managers?.map((role) => role.userName);

    const owners = useAppSelector((state) => state.store.storeState.watchedStore.roles)?.filter((role) => role.storeRole === StoreRoleEnum.OWNER);
    const owners_names = owners?.map((role) => role.userName);
    const user_name = managers.filter((manager) => manager.userId === userToFireId)[0]?.userName;


    //maybe take it from params
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior');
        dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleOnSubmit = () => {
        let response;
        switch (role) {
            case 'owner':
                response = dispatch(fireOwner(form.getValues()));
                break;
            case 'manager':
                response = dispatch(fireManager(form.getValues()));
                break;
            default:
                break;
        }
        handleOnClose();
    }

    const handleChange = (event: SelectChangeEvent) => {
        //dispatch(setFontSize(event.target.value as ThemeFontSize));
        switch (role) {
            case 'owner':
                setUserToFireId(owners.filter((owner) => owner.userName === event.target.value as string)[0].userId);
                break;
            case 'manager':
                setUserToFireId(managers.filter((manager) => manager.userName === event.target.value as string)[0].userId);
                break;
            default:
                break;
        }

    };

    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
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
                                {role === 'manager' ? <p>choose the manager u want to fire</p> : <p> choose the owner u want to fire </p>}
                            </Typography>
                        </Grid>
                        {role === 'manager' ?
                            <Grid item xs={12}>
                                <SelectAutoWidth label={'managers in the store'} values={managers_names} labels={managers_names} value={user_name} handleChange={handleChange} />
                            </Grid> : <Grid item xs={12}>
                                <SelectAutoWidth label={'owners in the store'} values={owners_names} labels={owners_names} value={user_name} handleChange={handleChange} />
                            </Grid>}


                        <Grid item xs={12}>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                fire
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