import { Dialog, Box, Grid, Typography, Button, TextField } from "@mui/material";
import { useCallback } from "react";
import { getStore } from "../../../reducers/storesSlice";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../../redux/store";
import { LoadingButton } from "@mui/lab";

const regularDiscount = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const handleOnClose = useCallback(() => {
        navigate(-1);
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    return (
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
                    display: 'flex',
                }}
            >
                <Grid
                    spacing={2}
                    container
                    component="form"

                >
                    <Grid item xs={12}>
                        <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                            Choose Discount Type
                        </Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            name="presentage"
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
                </Grid >
                <Box display={'flex'}>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => navigate('/dashboard/store/superior/RegularDiscount')}
                    >
                        Regular
                    </Button>

                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => navigate('/dashboard/store/superior/ConditionalDiscount')}
                    >
                        Composite
                    </Button>
                </Box>
            </Box>
        </Dialog >
    );
}
export default regularDiscount;