import { useState } from "react";
import { useForm } from "react-hook-form";
import { fireManagerFormValues } from "../../types/formsTypes";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, SelectChangeEvent } from "@mui/material";
import error from "../Alerts/error";
import AlertDialog from "../Dialog/AlertDialog";
import SelectAutoWidth from "../Selectors/AutoWidth";
import { StoreRoleEnum } from "../../types/systemTypes/StoreRole";

const FireManager = () => {
    const dispatch = useAppDispatch();
    const [open, setOpen] = useState(true);
    const [managerToFireId, setManagerToFireId] = useState(-1);
    const manager_name = '';// useAppSelector((state) => state.store.storeState.watchedStore.storeRoles).filter((role) => role.storeRole === StoreRoleEnum.MANAGER).map((role) => role.user.username);
    const form = useForm<fireManagerFormValues>();
    const navigate = useNavigate();
    const params = useParams();
    const userId = params.id ?? '-1';
    const isLoading = useAppSelector((state) => state.store.storeState.isLoading);
    const error = useAppSelector((state) => state.store.storeState.error);

    //maybe by id
    const storeId = useAppSelector((state) => state.store.storeState.watchedStore.id);
    const token = useAppSelector((state) => state.auth.token);
    //maybe take it from params
    const handleOnClose = () => {
        setOpen(false);
        navigate(-1);
        dispatch(getStoreData({ storeId: storeId, token: token }));

    }
    const handleOnSubmit = () => {
        const response = dispatch(deleteManager(form.getValues()));
        response.then((res) => {
            if (res.meta.requestStatus === 'fulfilled') {
                handleOnClose();
            }
        });
    }

    const handleChange = (event: SelectChangeEvent) => {
        //dispatch(setFontSize(event.target.value as ThemeFontSize));
        console.log(event.target.value);
        setManagerToFireId(managers.filter((manager) => manager.userName === event.target.value as string)[0].userId);

    };

    const managers = useAppSelector((state) => state.store.storeState.watchedStore.storeRoles).filter((role) => role.storeRole === StoreRoleEnum.MANAGER);
    const managers_names = managers.map((role) => role.userName);
    return (
        <>
            <Dialog onClose={handleOnClose} open={open}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 250,
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
                                choose the manager u want to fire
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <SelectAutoWidth label={'managers in the store'} values={managers_names} labels={managers_names} value={manager_name} handleChange={handleChange} />
                        </Grid>

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
                <AlertDialog open={!!error} onClose={() => { dispatch(clearStoreErrors()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );

}
export default FireManager;