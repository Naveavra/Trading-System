import { useCallback, useState } from "react";
import { useSelector } from "react-redux";
import { RootState, useAppDispatch } from "../../redux/store";
import { LoadingButton } from "@mui/lab";
import { Dialog, Box, Grid, Typography, SelectChangeEvent } from "@mui/material";
import { clearStoreError, getStore } from "../../reducers/storesSlice";
import AlertDialog from "../Dialog/AlertDialog";
import SelectAutoWidth from "../Selectors/AutoWidth";
import { useNavigate } from "react-router-dom";
import { deleteShoppingRule } from "../../reducers/ShoppingRules";
import { deleteDiscount } from "../../reducers/discountSlice";

interface DeletePolicyProps {
    policy: 'rule' | 'discount'
}
const DeletePolicy: React.FC<DeletePolicyProps> = ({ policy }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const [id, setId] = useState(-1);
    const [params, setParam] = useState('');
    const userId = useSelector((state: RootState) => state.auth.userId);
    const storeId = useSelector((state: RootState) => state.store.storeState.watchedStore.storeId);

    const discounts = useSelector((state: RootState) => state.store.storeState.watchedStore.discounts);
    const rules = useSelector((state: RootState) => state.store.storeState.watchedStore.purchasePolicies);
    const discount_names = discounts.map((discount) => discount.content);
    const rule_names = rules.map((rule) => rule.content);
    const isLoading = useSelector((state: RootState) => state.store.storeState.isLoading);
    const error = useSelector((state: RootState) => state.store.storeState.error);


    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior');
        dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleOnSubmit = () => {
        if (policy === 'discount') {
            dispatch(deleteDiscount({ userId: userId, storeId: storeId, discountId: id }));
        }
        else {
            dispatch(deleteShoppingRule({ userId: userId, storeId: storeId, purchasePolicyId: id }));
        }
        handleOnClose();
    }

    const handleChange = (event: SelectChangeEvent) => {
        setParam(event.target.value as string);
        setId(Number(policy === 'discount' ? discounts.filter((discount) => discount.content === event.target.value)[0]?.id : rules.filter((rule) => rule.content === event.target.value)[0]?.id));
    };


    return (
        <>
            <Dialog onClose={handleOnClose} open={true} >
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
                                {`delete ${policy}`}
                            </Typography>
                        </Grid>
                        {policy === 'rule' ?
                            <Grid item xs={12} >
                                <SelectAutoWidth label={'purchase roles in the store'} values={rule_names} labels={rule_names} value={params} handleChange={handleChange} />
                            </Grid> : <Grid item xs={12} >
                                <SelectAutoWidth label={'owners in the store'} values={discount_names} labels={discount_names} value={params} handleChange={handleChange} />
                            </Grid>}
                        <Grid item xs={12} >
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
                <AlertDialog open={!!error} onClose={() => { dispatch(clearStoreError({})); }} text={error} sevirity={"error"} />
                : null}
        </>

    );
}
export default DeletePolicy;