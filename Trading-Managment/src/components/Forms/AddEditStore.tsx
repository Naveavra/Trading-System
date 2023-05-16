import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, TextField } from "@mui/material";
import { useForm } from "react-hook-form";
import { clearProductError } from "../../reducers/productsSlice";
import { storeFormValues } from "../../types/formsTypes";
import error from "../Alerts/error";
import AlertDialog from "../Dialog/AlertDialog";
import { useState } from "react";
import { RootState, useAppDispatch, useAppSelector } from "../../redux/store";
import { useNavigate } from "react-router-dom";
import { postStore, patchStore, getStore } from "../../reducers/storesSlice";
import { Action } from "../../types/systemTypes/Action";

interface storeProps {
    mode: 'add' | 'edit';
};
const AddEditStore: React.FC<storeProps> = ({ mode }) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [open, setOpen] = useState(true);
    const form = useForm<storeFormValues>();
    const required = (() => { return mode === 'add' ? true : false })();
    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const isLoading = useAppSelector((state: RootState) => state.store.isLoading);
    const error = useAppSelector((state: RootState) => state.store.error);
    const store = useAppSelector((state: RootState) => state.store.storeState.watchedStore);
    const store_id = store ? store.id : -1;
    const permmisions = useAppSelector((state: RootState) => state.auth.permmisions).filter((perm) => perm.storeId === store_id);
    const Actions = permmisions.length > 0 ? permmisions[0].actions : [];
    const canClose = Actions.includes(Action.closeStore);
    const handleOnSubmit = () => {
        form.setValue('userId', userId);
        let response;
        switch (mode) {
            case 'add':
                response = dispatch(postStore(form.getValues()));
                break;
            case 'edit':
                form.setValue('storeId', store_id);
                response = dispatch(patchStore(form.getValues()));
                break;
            default:
                break;
        }

    }
    const handleOnClose = () => {
        setOpen(false);
        navigate("/dashboard");
        dispatch(getStore({ userId: userId, storeId: store_id }));
    }
    return (
        <>
            <Dialog onClose={handleOnClose} open={open}>
                <Box
                    sx={{
                        marginTop: 4,
                        top: '50%',
                        left: '50%',
                        height: 400,
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
                                {mode === 'add' ? <span>add store</span> : <span>edit store</span>}
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="name"
                                type="text"
                                fullWidth
                                label="name"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('name', {
                                        required: {
                                            value: required,
                                            message: "name is required"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['name'] ?? false}
                                helperText={form.formState.errors['name']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="description"
                                type="text"
                                fullWidth
                                label="description"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('desc', {
                                        required: {
                                            value: required,
                                            message: "descreaption is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['desc'] ?? false}
                                helperText={form.formState.errors['desc']?.message ?? undefined}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                name="img"
                                type="text"
                                fullWidth
                                label="img"
                                sx={{ mt: 1, mb: 1 }}
                                inputProps={{
                                    ...form.register('img', {
                                        required: {
                                            value: required,
                                            message: "img is mendatory"
                                        }
                                    })
                                }}
                                error={!!form.formState.errors['img'] ?? false}
                                helperText={form.formState.errors['img']?.message ?? undefined}
                            />
                        </Grid>
                        {mode === 'edit' && canClose ?
                            <Grid item xs={12}>
                                <TextField
                                    name="isActive"
                                    type='boolean'
                                    fullWidth
                                    label="isActive"
                                    sx={{ mt: 1, mb: 1 }}
                                    inputProps={{
                                        ...form.register('isActive', {
                                            required: {
                                                value: required,
                                                message: "img is mendatory"
                                            }
                                        })
                                    }}
                                    error={!!form.formState.errors['isActive'] ?? false}
                                    helperText={form.formState.errors['isActive']?.message ?? undefined}
                                />
                            </Grid> : null
                        }
                        <Grid item xs={12}>
                            <LoadingButton
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                                loading={isLoading}
                            >
                                {mode === 'add' ? <span>add store</span> : <span>edit store</span>}
                            </LoadingButton>
                        </Grid>
                    </Grid >
                </Box>
            </Dialog >
            {!!error ?
                <AlertDialog open={!!error} onClose={() => { dispatch(clearProductError()); }} text={error} sevirity={"error"} />
                : null}
        </>
    );
}
export default AddEditStore;