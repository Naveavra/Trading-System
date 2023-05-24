import { Dialog, Box, Grid, Typography, Button } from "@mui/material";
import { useCallback } from "react";
import { getStore } from "../../../reducers/storesSlice";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../../redux/store";
import { LoadingButton } from "@mui/lab";

const mainScreen = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior');
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    return (
        <Dialog onClose={handleOnClose} open={true}>
            <Box
                sx={{
                    marginTop: 4,
                    top: '50%',
                    left: '50%',
                    height: 150,
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
                <Grid item xs={12}>
                    <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                        Choose Discount Type
                    </Typography>
                </Grid>
                <Box display={'flex'}>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => navigate('/dashboard/store/superior/regularDiscount')}
                    >
                        Regular
                    </Button>

                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => navigate('/dashboard/store/superior/conditionalDiscount')}
                    >
                        Composite
                    </Button>
                </Box>
            </Box>
        </Dialog >
    );
}
export default mainScreen;