

import { Dialog, Box, Grid, Typography, Button, TextField } from "@mui/material";
import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RootState, useAppDispatch, useAppSelector } from "../../../redux/store";
import { addPredicateToRegularDiscount, addRegularDiscount, setParamsToTmpPredicate, setPredicateTypeToTmpPredicate, setComposoreToTmpPredicate, clearTmpPredicate, cleanRegularDiscount } from "../../../reducers/discountSlice";
import { Composore, PredicateDataObject, PredicateType } from "../../../types/systemTypes/Discount";

const addPredicate = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [firstType, setFirstType] = useState('');
    const [secondType, setSecondType] = useState('');
    const [thirdType, setThirdType] = useState('');
    const [last, setLast] = useState(false);

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const storeId = useAppSelector((state: RootState) => state.store.storeState.watchedStore.storeId);
    const percentage = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.percentage);
    const discountType = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.discountType);
    const prodId = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.prodId);
    const discountedCategory = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.discountedCategory);
    const predicates = useAppSelector((state: RootState) => state.discount?.currentRegularDiscount.predicates);
    const predicate: PredicateDataObject = useAppSelector((state: RootState) => state.discount?.tmpPredicate);

    const handleOnClose = useCallback(() => {
        navigate(-1);
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleMin = () => {
        setFirstType('Min');
    }
    const handleMax = () => {
        setFirstType('Max');
    }
    const handleOnPrice = () => {
        setSecondType('Price');
        firstType === 'Min' ? dispatch(setPredicateTypeToTmpPredicate(PredicateType.MinPrice)) : dispatch(setPredicateTypeToTmpPredicate(PredicateType.MaxPrice));
    }
    const handleOnItem = () => {
        setSecondType('Item');
        firstType === 'Min' ? dispatch(setPredicateTypeToTmpPredicate(PredicateType.MinNumOfItem)) : dispatch(setPredicateTypeToTmpPredicate(PredicateType.MaxNumOfItem));
    }
    const handleOnCategory = () => {
        setSecondType('Category');
        firstType === 'Min' ? dispatch(setPredicateTypeToTmpPredicate(PredicateType.MinNumFromCategory)) : dispatch(setPredicateTypeToTmpPredicate(PredicateType.MaxNumFromCategory));
    }
    const handleSetProductId = (input: string) => {
        console.log(input);
        dispatch(setParamsToTmpPredicate(input));
    }
    const handleSetCategory = (input: string) => {
        console.log(input);
        dispatch(setParamsToTmpPredicate(input));
    }
    const handleSetPrice = (input: string) => {
        console.log(input);
        dispatch(setParamsToTmpPredicate(input));
    }
    const handleOnSubmit = () => {
        dispatch(addPredicateToRegularDiscount(predicate));
        dispatch(addRegularDiscount({
            storeId: userId,
            userId: storeId,
            percentage: percentage,
            discountType: discountType,
            prodId: prodId,
            discountedCategory: discountedCategory,
            predicates: predicates,
        }));
        dispatch(clearTmpPredicate());
        dispatch(cleanRegularDiscount());
        navigate("/dashboard/store/superior");
    };
    const handleAddComposore = () => {
        setLast(true);
    }
    const handleAnd = () => {
        setThirdType('And');
        dispatch(setComposoreToTmpPredicate(Composore.AND));
    }
    const handleOr = () => {
        setThirdType('Or');
        dispatch(setComposoreToTmpPredicate(Composore.OR));
    }
    const handleXor = () => {
        setThirdType('Xor');
        dispatch(setComposoreToTmpPredicate(Composore.XOR));
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
                <Grid item xs={12}>
                    <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                        choose predicate type
                    </Typography>
                </Grid>
                <Box display={'flex'}>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={handleMin}
                        color={firstType === 'Min' ? 'success' : 'primary'}
                    >
                        min
                    </Button>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={handleMax}
                        color={firstType === 'Max' ? 'success' : 'primary'}
                    >
                        max
                    </Button>
                </Box>
                {firstType != '' ?
                    <>
                        <Box display={'flex'}>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleOnPrice}
                                color={secondType === 'Price' ? 'success' : 'primary'}
                            >
                                on price
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleOnItem}
                                color={secondType === 'Item' ? 'success' : 'primary'}
                            >
                                on item
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleOnCategory}
                                color={secondType === 'Category' ? 'success' : 'primary'}
                            >
                                on category
                            </Button>
                        </Box>
                    </>
                    : null
                }

                {secondType === 'Item' ?
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="outlined-required"
                            label="enter product id"
                            onChange={(e) => { handleSetProductId(e.target.value) }}
                        />
                    </Grid> : null}
                {secondType === 'Category' ?
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="outlined-required"
                            label="enter category"
                            onChange={(e) => { handleSetCategory(e.target.value) }}
                        />
                    </Grid> : null}
                {secondType === 'Price' ?
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="outlined-required"
                            label="enter price"
                            onChange={(e) => { handleSetPrice(e.target.value) }}
                        />
                    </Grid> : null}
                {last ?
                    <>
                        <Box display={'flex'}>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleAnd}
                                color={thirdType === 'And' ? 'success' : 'primary'}
                            >
                                And
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleOr}
                                color={thirdType === 'Or' ? 'success' : 'primary'}
                            >
                                Or
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleXor}
                                color={thirdType === 'Xor' ? 'success' : 'primary'}
                            >
                                Xor
                            </Button>
                        </Box>
                    </>

                    : null}
                {firstType != '' && secondType != '' ?
                    <Box display={'flex'}>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleAddComposore}
                            color={last ? 'success' : 'primary'}
                            disabled={last}
                        >
                            composore predicate
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
            </Box>
        </Dialog >
    )
}
export default addPredicate;