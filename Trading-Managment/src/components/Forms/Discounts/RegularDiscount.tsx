import { Dialog, Box, Grid, Typography, Button, TextField } from "@mui/material";
import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RootState, useAppDispatch, useAppSelector } from "../../../redux/store";
import { addRegularDiscount, addRegularDiscountToSource, setCategoryToRegularDiscount, setDiscountTypeToRegularDiscount, setpercentageToRegularDiscount, setProductIdToRegularDiscount, setSourceForPredicate, setSourceToRegularDiscount } from "../../../reducers/discountSlice";

interface props {
    tree: boolean;
}
const regularDiscount: React.FC<props> = ({ tree }) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [error, setError] = useState('');
    const [type, setType] = useState('');
    const [buttonColor, setButtonColor] = useState('primary');

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const storeId = useAppSelector((state: RootState) => state.store.storeState.watchedStore.storeId);
    const percentage = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.percentage);
    const discountType = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.discountType);
    const prodId = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.prodId);
    const discountedCategory = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.discountedCategory);
    const predicates = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.predicates);
    const [sorce, setSorce] = useState('');

    const handleOnClose = useCallback(() => {
        navigate(-1);
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleSetSource = (input: string) => {
        //dispatch(setSourceToRegularDiscount(input));
        setSorce(input);
    }
    const handleSetpercentage = (input: string) => {
        const percentage = parseFloat(input);
        if (percentage > 100 || percentage < 0) {
            setError('percentage must be between 0 to 100');
        }
        else {
            setError('');
            console.log(percentage);
            dispatch(setpercentageToRegularDiscount(percentage));
        }
    }
    const handleOnProduct = () => {
        dispatch(setDiscountTypeToRegularDiscount('Product'));
        setType('Product');
    }
    const handleOnStore = () => {
        dispatch(setDiscountTypeToRegularDiscount('Store'));
        setType('Store');

    }
    const handleOnCategory = () => {
        dispatch(setDiscountTypeToRegularDiscount('Category'));
        setType('Category');
    }
    const handleSetProductId = (input: string) => {
        const productId = parseInt(input);
        dispatch(setProductIdToRegularDiscount(productId));
    }
    const handleSetCategory = (input: string) => {
        dispatch(setCategoryToRegularDiscount(input));
    }
    const handleOnSubmit = () => {
        debugger;
        if (tree) {
            dispatch(addRegularDiscountToSource({
                source: sorce,
                storeId: userId,
                userId: storeId,
                percentage: percentage,
                discountType: discountType,
                prodId: prodId,
                discountedCategory: discountedCategory,
                predicates: predicates,
            }));
        }
        else {
            dispatch(addRegularDiscount({
                storeId: userId,
                userId: storeId,
                percentage: percentage,
                discountType: discountType,
                prodId: prodId,
                discountedCategory: discountedCategory,
                predicates: predicates,
            }));
        }
        navigate(-1);
    };
    const handleAddPredicate = () => {
        dispatch(setSourceForPredicate(sorce));
        navigate("addPredicate");
    }


    return (
        <Dialog onClose={handleOnClose} open={true}>
            <Box
                sx={{
                    marginTop: 4,
                    top: '50%',
                    left: '50%',
                    height: 450,
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

                {tree ?
                    <>
                        <Grid item xs={12} sx={{ mt: 2 }}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                enter percentage
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                required
                                id="outlined-required"
                                label="root source"
                                error={error != ''}
                                onChange={(e) => { handleSetSource(e.target.value) }}
                            />
                        </Grid>
                    </>
                    : null
                }
                <Grid item xs={12} sx={{ mt: 2 }}>
                    <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                        enter percentage
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        required
                        id="outlined-required"
                        label="percentage"
                        error={error != ''}
                        helperText={error != '' ? error : "enter percentage"}
                        onChange={(e) => { handleSetpercentage(e.target.value) }}
                    />
                </Grid>
                <Box display={'flex'}>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={handleOnProduct}
                        color={type === 'Product' ? 'success' : 'primary'}
                    >
                        on Product
                    </Button>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={handleOnStore}
                        color={type === 'Store' ? 'success' : 'primary'}
                    >
                        on store
                    </Button>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={handleOnCategory}
                        color={type === 'Category' ? 'success' : 'primary'}
                    >
                        on category
                    </Button>
                </Box>
                {error === '' && type != '' && type === 'Product' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                required
                                id="outlined-required"
                                label="enter product id"
                                onChange={(e) => { handleSetProductId(e.target.value) }}
                            />
                        </Grid>
                    </>
                    : null
                }
                {error === '' && type != '' && type === 'Category' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                required
                                id="outlined-required"
                                label="enter category"
                                onChange={(e) => { handleSetCategory(e.target.value) }}
                            />
                        </Grid>
                    </>
                    : null
                }
                {error == '' && type != '' ?
                    <Box display={'flex'}>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleAddPredicate}
                        >
                            add predicate
                        </Button>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleOnSubmit}
                        >
                            submit
                        </Button>
                    </Box> : null}
                {predicates.length > 0 ?
                    <Box display={'flex'}>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleOnSubmit}
                        >
                            submit
                        </Button>
                    </Box> : null}
            </Box>
        </Dialog >
    );
}
export default regularDiscount;