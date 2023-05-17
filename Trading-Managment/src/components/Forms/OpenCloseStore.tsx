import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import AlertDialog from "../Dialog/AlertDialog";
import { useForm } from "react-hook-form";
import { appointUserFormValues } from "../../types/formsTypes";
import { useNavigate } from "react-router-dom";
import { appointManager, appointOwner, clearStoreError, patchStore } from "../../reducers/storesSlice";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { useState } from "react";


const OpenCloseStore = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [open, setOpen] = useState(true);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);
    const store = useAppSelector((state: RootState) => state.store.storeState.watchedStore);
    const mode = store.isActive;
    const storeId = store.storeId;
    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const handleOnClose = () => {
        setOpen(false);
        navigate(-1);
        // dispatchEvent(getStoreData({}))
    }
    const handleOnSubmit = () => {
        let response;
        switch (mode) {
            case true:
                response = dispatch(patchStore({ isActive: false, storeId: storeId, userId: userId, img: store.img, desc: store.description }));
                response.then((res: { meta: { requestStatus: string; }; }) => {
                    if (res.meta.requestStatus === 'fulfilled') {
                        handleOnClose();
                    }
                });
            case false:
                response = dispatch(patchStore({ isActive: true, storeId: storeId, userId: userId, img: store.img, desc: store.description }));
                response.then((res: { meta: { requestStatus: string; }; }) => {
                    if (res.meta.requestStatus === 'fulfilled') {
                        handleOnClose();
                    }
                });
        }
    }
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
                                {mode ? 'are u shure u want to close the store' : 'are u shure u want to open the store'}
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                {mode ? 'close' : 'open'}
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
export default OpenCloseStore;