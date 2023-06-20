import { Dialog, Box, Grid, Typography, Button, TextField } from "@mui/material";
import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RootState, useAppDispatch, useAppSelector } from "../../../redux/store";
import { addParamsToTmpPredicate, addPredicateToRegularDiscount, addRegularDiscount, addRegularDiscountToSource, cleanRegularDiscount, clearTmpPredicate, removeFromPredicate, setCategoryToRegularDiscount, setComposoreToTmpPredicate, setDiscountTypeToRegularDiscount, setParamsToTmpPredicate, setpercentageToRegularDiscount, setPredicateTypeToTmpPredicate, setProductIdToRegularDiscount, setSourceForPredicate, setSourceToRegularDiscount } from "../../../reducers/discountSlice";
import { Composore, PredicateType } from "../../../types/systemTypes/Discount";

interface props {
    tree: boolean;
}
const regularDiscount: React.FC<props> = ({ tree }) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [error, setError] = useState('');
    const [type, setType] = useState('');

    //predicares
    const [toAddPredicate, setToAddPredicate] = useState(false);
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
    const tmpPredicate = useAppSelector((state) => state.discount.tmpPredicate);

    const [secParam, setSecParam] = useState(false);
    const [sorce, setSorce] = useState('');
    const [description, setDescription] = useState('');
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
        if (tree) {
            dispatch(addRegularDiscountToSource({
                source: sorce,
                storeId: storeId,
                userId: userId,
                percentage: percentage,
                discountType: discountType,
                prodId: prodId,
                discountedCategory: discountedCategory,
                predicates: predicates,
            }));
            dispatch(cleanRegularDiscount());
            navigate('/dashboard/store/superior/conditionalDiscount/leafs');
        }
        else {
            dispatch(addRegularDiscount({
                storeId: storeId,
                userId: userId,
                description: description,
                percentage: percentage,
                discountType: discountType,
                prodId: prodId,
                discountedCategory: discountedCategory,
                predicates: predicates,
            }));
            dispatch(cleanRegularDiscount());
            navigate('/dashboard/store/superior');
        }

    };
    const handleAddPredicate = () => {
        if (tree) {
            dispatch(setSourceForPredicate(sorce));
        }
        // navigate("addPredicate");
        setToAddPredicate(true);
    }
    //predicates methods
    const handleMin = () => {
        setFirstType('Min');
    }
    const handleMax = () => {
        setFirstType('Max');
    }
    const handleOnPredicatePrice = () => {
        setSecondType('Price');
        setSecParam(false);
        firstType === 'Min' ? dispatch(setPredicateTypeToTmpPredicate(PredicateType.MinPrice)) : dispatch(setPredicateTypeToTmpPredicate(PredicateType.MaxPrice));
    }
    const handleOnPredicateItem = () => {
        setSecondType('Item');
        setSecParam(false);
        firstType === 'Min' ? dispatch(setPredicateTypeToTmpPredicate(PredicateType.MinNumOfItem)) : dispatch(setPredicateTypeToTmpPredicate(PredicateType.MaxNumOfItem));
    }
    const handleOnPredicateCategory = () => {
        setSecondType('Category');
        setSecParam(false);
        firstType === 'Min' ? dispatch(setPredicateTypeToTmpPredicate(PredicateType.MinNumFromCategory)) : dispatch(setPredicateTypeToTmpPredicate(PredicateType.MaxNumFromCategory));
    }
    const setParamsToPredicate = (input: string) => {
        console.log(input);
        setSecParam(true);
        dispatch(setParamsToTmpPredicate(input));
    }
    const addParamsToPredicate = (input: string) => {
        dispatch(addParamsToTmpPredicate(input));
    }
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
    const handleNull = () => {
        setThirdType('Null');
        dispatch(setComposoreToTmpPredicate(Composore.NULL));
    }
    const handleOnSubmitPredicate = () => {
        setFirstType('');
        setSecondType('');
        setThirdType('');
        setToAddPredicate(false);
        setLast(false);
        dispatch(addPredicateToRegularDiscount(tmpPredicate))
        dispatch(clearTmpPredicate());
    }


    return (
        <Dialog onClose={handleOnClose} open={true}>
            <Box
                sx={{
                    marginTop: 4,
                    top: '50%',
                    left: '50%',
                    height: 800,
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
                                enter source node
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
                    : <>
                        <Grid item xs={12} sx={{ mt: 2 }}>
                            <Typography component="h1" sx={{ alignContent: 'center', align: 'center', textAlign: 'center' }} >
                                enter description
                            </Typography>
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                required
                                id="outlined-required"
                                label="description"
                                onChange={(e) => { setDescription(e.target.value) }}
                            />
                        </Grid>
                    </>
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
                {toAddPredicate ?
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
                    : null
                }
                {firstType != '' ?
                    <>
                        <Box display={'flex'}>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleOnPredicatePrice}
                                color={secondType === 'Price' ? 'success' : 'primary'}
                            >
                                on price
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleOnPredicateItem}
                                color={secondType === 'Item' ? 'success' : 'primary'}
                            >
                                on item
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleOnPredicateCategory}
                                color={secondType === 'Category' ? 'success' : 'primary'}
                            >
                                on category
                            </Button>
                        </Box>
                    </>
                    : null
                }
                {secondType === 'Item' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                required
                                id="outlined-required"
                                label="enter product id"
                                onChange={(e) => { setParamsToPredicate(e.target.value) }}
                            />
                        </Grid>
                        {secParam ?
                            <Grid item xs={12} sx={{ mt: 2 }}>
                                <TextField
                                    required
                                    id="outlined-required"
                                    label="enter amount"
                                    onChange={(e) => { addParamsToPredicate(e.target.value) }}
                                />
                            </Grid> : null}
                    </> : null}
                {secondType === 'Category' ?
                    <>

                        <Grid item xs={12}>
                            <TextField
                                required
                                id="outlined-required"
                                label="enter category"
                                onChange={(e) => { setParamsToPredicate(e.target.value) }}
                            />
                        </Grid>
                        {secParam ?
                            <Grid item xs={12} sx={{ mt: 2 }}>
                                <TextField
                                    required
                                    id="outlined-required"
                                    label="enter amount"
                                    onChange={(e) => { addParamsToPredicate(e.target.value) }}
                                />
                            </Grid> : null}
                    </>

                    : null}
                {secondType === 'Price' ?
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="outlined-required"
                            label="enter price"
                            onChange={(e) => {
                                debugger;
                                setParamsToPredicate(e.target.value);
                                addParamsToPredicate('');
                                dispatch(removeFromPredicate(''));
                            }}
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
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={handleNull}
                                color={thirdType === 'Null' ? 'success' : 'primary'}
                            >
                                Null
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
                            onClick={handleOnSubmitPredicate}
                        >
                            add predicate
                        </Button>
                    </Box> : null}
            </Box>
        </Dialog >
    );
}
export default regularDiscount;