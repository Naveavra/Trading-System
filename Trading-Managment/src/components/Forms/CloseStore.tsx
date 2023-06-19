
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useCallback, useState } from "react";

import AlertDialog from "../Dialog/AlertDialog";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { clearAdminError, closeStorePerminently } from "../../reducers/adminSlice";
import { closeStoreFormValues } from "../../types/formsTypes";


import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField, SelectChangeEvent } from "@mui/material";
import { getClientData } from "../../reducers/authSlice";
import SelectAutoWidth from "../Selectors/AutoWidth";


const CloseStore = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const form = useForm<closeStoreFormValues>();
    const [value, setValue] = useState('');
    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);
    const stores = useAppSelector((state: RootState) => state.store.storeInfoResponseData);
    const storesNames = stores.map(store => store.name);


    const handleOnClose = useCallback(() => {
        navigate('/dashboard/admin');
        dispatch(getClientData({ userId: userId }));
    }, []);

    const handleOnSubmit = () => {
        form.setValue("userId", userId);
        form.setValue("storeId", stores?.filter(store => store.name === value)[0].storeId);
        dispatch(closeStorePerminently(form.getValues()));
        handleOnClose();
    }
    const handleChange = (event: SelectChangeEvent) => {
        setValue(event.target.value as string);
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={true}>
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
                                please choose the store
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <SelectAutoWidth label={'stores in system'} values={storesNames} labels={storesNames} value={value} handleChange={handleChange} />
                        </Grid>
                        <Grid item xs={12}>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                delete
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>

            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearAdminError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );
}
export default CloseStore;