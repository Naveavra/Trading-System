

import { Dialog, Box, Grid, Typography, Button, TextField } from "@mui/material";
import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RootState, useAppDispatch, useAppSelector } from "../../../redux/store";
import { addPurchasePolicy, addTmpToCurrent, clearCurrentShoppingRules, clearTmpShopRule, setDescritionToCurrent, setTmpShopRule } from "../../../reducers/ShoppingRules";
import { TimeClock } from "@mui/x-date-pickers";
import { DemoContainer, DemoItem } from '@mui/x-date-pickers/internals/demo';
import dayjs, { Dayjs } from 'dayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers';

const AddPurchasePolicy = () => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const [type, setType] = useState('');
    const [limiter, setLimiter] = useState('');
    const [timeType, setTimeType] = useState('');
    const [last, setLast] = useState(false);

    const [description, setDescription] = useState('');
    const [category, setCategory] = useState('');
    const [productId, setProductId] = useState(-1);
    const [amount, setAmount] = useState(-1);
    const [timeLimit, setTimeLimit] = useState('');
    const [age, setAge] = useState(-1);
    const [composore, setComposore] = useState('');

    const [timeValue, settimeValue] = useState<Dayjs | null>(dayjs('2022-04-17T15:30'));

    const userId = useAppSelector((state: RootState) => state.auth.userId);
    const storeId = useAppSelector((state: RootState) => state.store.storeState.watchedStore.storeId);
    const purchasePolicy = useAppSelector((state: RootState) => state.shoppingRule.currentShoppingRule);
    const handleOnClose = useCallback(() => {
        navigate('/dashboard/store/superior');
        dispatch(clearTmpShopRule());
        dispatch(clearCurrentShoppingRules());
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleMin = () => {
        setLimiter('Min');
    }
    const handleMax = () => {
        setLimiter('Max');
    }

    const handleType = (input: string) => {
        setType(input);
    }
    const handleDescription = (input: string) => {
        setDescription(input);
        dispatch(setDescritionToCurrent(input));
    }

    const handleOnSubmit = () => {
        switch (type) {
            case 'Category':
                dispatch(setTmpShopRule({ type: 'category', category: category, amount: amount, limiter: limiter }));
                break;
            case 'Item':
                dispatch(setTmpShopRule({ type: 'item', productId: productId, amount: amount, limiter: limiter }));
                break;
            case 'Date Time':
                dispatch(setTmpShopRule({ type: 'dateTime', category: category, timeLimit: timeLimit, timeType: timeType, limiter: limiter }));
                break;
            case 'User':
                dispatch(setTmpShopRule({ type: 'user', ageLimite: age, productId: productId, limiter: limiter }));
                break;
            case 'Basket':
                dispatch(setTmpShopRule({ type: 'basket', productId: productId, amount: amount }));
                break;
        }
        dispatch(addTmpToCurrent());
        handleFinish();

    };
    const handleFinish = () => {
        dispatch(addPurchasePolicy({ userId: userId, storeId: storeId, purchasePolicy: purchasePolicy }));
        dispatch(clearTmpShopRule());
        dispatch(clearCurrentShoppingRules());
        handleClean();
        handleOnClose();
    }
    const handleAddComposore = (input: string) => {
        setComposore(input);
        switch (type) {
            case 'Category':
                dispatch(setTmpShopRule({ type: 'category', category: category, amount: amount, limiter: limiter, composore: input }));
                break;
            case 'Item':
                dispatch(setTmpShopRule({ type: 'item', productId: productId, amount: amount, limiter: limiter, composore: input }));
                break;
            case 'Date Time':
                dispatch(setTmpShopRule({ type: 'dateTime', category: category, timeLimit: timeLimit, timeType: timeType, limiter: limiter, composore: input }));
                break;
            case 'User':
                dispatch(setTmpShopRule({ type: 'user', ageLimite: age, productId: productId, limiter: limiter, composore: input }));
                break;
            case 'Basket':
                dispatch(setTmpShopRule({ type: 'basket', productId: productId, amount: amount, composore: input }));
                break;
            case 'Null':
                dispatch(setTmpShopRule({ type: 'null', productId: productId, amount: amount, composore: input }));
                break;

        }
        dispatch(addTmpToCurrent());
        dispatch(clearTmpShopRule());
        handleClean();
    }
    const handleClean = () => {
        setType('');
        setLimiter('');
        setTimeType('');
        setLast(false);
        //setDescription('');
        setCategory('');
        setProductId(-1);
        setAmount(-1);
        setTimeLimit('');
        setAge(-1);
        setComposore('');
    }
    const monthToInt = new Map<string, string>([
        ['Jan', '01'],
        ['Feb', '02'],
        ['Mar', '03'],
        ['Apr', '04'],
        ['May', '05'],
        ['Jun', '06'],
        ['Jul', '07'],
        ['Aug', '08'],
        ['Sep', '09'],
        ['Oct', '10'],
        ['Nov', '11'],
        ['Dec', '12'],
    ]);
    // Form Buttons


    const getDtae = (day: string, month: string, year: string): string => {
        return `${day}/${monthToInt.get(month)}/${year}`;
    }

    const handleDate = (date: React.SetStateAction<Date | null>) => {
        if (date) {
            const arr = date.toString().split(' ');
            setTimeLimit(getDtae(arr[2], arr[1], arr[3]));
        }
    }
    const handleTime = (time: dayjs.Dayjs | null) => {
        if (time) {
            console.log(time);
            const realTime = time.toString().split(' ')[4];
            console.log(realTime);
            settimeValue(time);
            setTimeLimit(realTime);
        }
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
                        create new purchase policy
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        sx={{ mt: 2, mb: 2 }}
                        required
                        id="outlined-required"
                        label="enter description"
                        onChange={(e) => { handleDescription(e.target.value) }}
                    />
                </Grid>
                <Box display={'flex'}>
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => handleType('Category')}
                        color={type === 'Category' ? 'success' : 'primary'}
                    >
                        Category
                    </Button>
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => handleType('Item')}
                        color={type === 'Item' ? 'success' : 'primary'}
                    >
                        Item
                    </Button>
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => handleType('Date Time')}
                        color={type === 'Date Time' ? 'success' : 'primary'}
                    >
                        Date Time
                    </Button>
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => handleType('User')}
                        color={type === 'User' ? 'success' : 'primary'}
                    >
                        User
                    </Button>
                    <Button
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={() => handleType('Basket')}
                        color={type === 'Basket' ? 'success' : 'primary'}
                    >
                        Basket
                    </Button>
                </Box>

                {type === 'Category' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                sx={{ mt: 2, mb: 2 }}
                                required
                                id="outlined-required"
                                label="enter category"
                                onChange={(e) => { setCategory(e.target.value) }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter amount"
                                onChange={(e) => { setAmount(parseInt(e.target.value)) }}
                            />
                        </Grid>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMin}
                            color={limiter === 'Min' ? 'success' : 'primary'}
                        >
                            Min
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMax}
                            color={limiter === 'Max' ? 'success' : 'primary'}
                        >
                            Max
                        </Button>
                    </>
                    : null}
                {type === 'Item' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter product id"
                                onChange={(e) => { setProductId(parseInt(e.target.value)) }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter amount"
                                onChange={(e) => { setAmount(parseInt(e.target.value)) }}
                            />
                        </Grid>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMin}
                            color={limiter === 'Min' ? 'success' : 'primary'}
                        >
                            Min
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMax}
                            color={limiter === 'Max' ? 'success' : 'primary'}
                        >
                            Max
                        </Button>
                    </>
                    : null}
                {type === 'Date Time' ?
                    <>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={() => setTimeType('Time Limit')}
                            color={timeType === 'Time Limit' ? 'success' : 'primary'}
                        >
                            Time Limit
                        </Button>

                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={() => setTimeType('Date Limit')}
                            color={timeType === 'Date Limit' ? 'success' : 'primary'}
                        >
                            Date Limit
                        </Button>
                    </>

                    : null
                }
                {type === 'Date Time' && timeType !== '' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter category"
                                onChange={(e) => { setCategory(e.target.value) }}
                            />
                        </Grid>
                        {timeType === 'Time Limit' ?
                            <Box sx={{ width: 300, height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                                <LocalizationProvider dateAdapter={AdapterDayjs}>
                                    <DemoContainer components={['TimeClock', 'TimeClock']}>
                                        <DemoItem label="enter the hour and the minute">
                                            <TimeClock value={timeValue} onChange={(newValue) => {
                                                handleTime(newValue);
                                            }
                                            } />
                                        </DemoItem>
                                    </DemoContainer>
                                </LocalizationProvider>
                            </Box>
                            : timeType === 'Date Limit' ?
                                <Grid item xs={12}>
                                    <DatePicker label={'birthday'} onChange={handleDate} sx={{ width: '100%' }} />
                                </Grid>
                                : null
                        }
                        {/* <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label={timeType === 'Time Limit' ? "enter time limit" : "enter date limit"}
                                onChange={(e) => { setTimeLimit(e.target.value) }}
                            />
                        </Grid> */}
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMin}
                            color={limiter === 'Min' ? 'success' : 'primary'}
                        >
                            Min
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMax}
                            color={limiter === 'Max' ? 'success' : 'primary'}
                        >
                            Max
                        </Button>
                    </>
                    : null
                }
                {type === 'User' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter age limit"
                                onChange={(e) => { setAge(parseInt(e.target.value)) }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter product id"
                                onChange={(e) => { setProductId(parseInt(e.target.value)) }}
                            />
                        </Grid>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMin}
                            color={limiter === 'Min' ? 'success' : 'primary'}
                        >
                            Min
                        </Button>
                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={handleMax}
                            color={limiter === 'Max' ? 'success' : 'primary'}
                        >
                            Max
                        </Button>
                    </>
                    : null}
                {type === 'Basket' ?
                    <>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter product id"
                                onChange={(e) => { setProductId(parseInt(e.target.value)) }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                required
                                sx={{ mt: 2, mb: 2 }}
                                id="outlined-required"
                                label="enter amount"
                                onChange={(e) => { setAmount(parseInt(e.target.value)) }}
                            />
                        </Grid>
                    </>
                    : null
                }
                {(category !== '' && amount !== -1) || (productId !== -1 && amount !== -1) || (category !== '' && timeType !== '') || (age !== -1 && productId !== -1) ?
                    <Box display={'flex'}>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                            onClick={() => setLast(true)}
                            color={last ? 'success' : 'primary'}
                            disabled={last}
                        >
                            composore predicate
                        </Button>
                    </Box> : null}
                {last ?
                    <>
                        <Box display={'flex'}>
                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => handleAddComposore('And')}
                                color={composore === 'And' ? 'success' : 'primary'}
                            >
                                And
                            </Button>
                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => handleAddComposore('Or')}
                                color={composore === 'Or' ? 'success' : 'primary'}
                            >
                                Or
                            </Button>
                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => handleAddComposore('Conditional')}
                                color={composore === 'Conditional' ? 'success' : 'primary'}
                            >
                                Conditional
                            </Button>
                            <Button
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => handleAddComposore('Null')}
                                color={composore === 'Null' ? 'success' : 'primary'}
                            >
                                Null
                            </Button>
                        </Box>
                    </>
                    : null}
                {purchasePolicy.type.length > 0 ?
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={handleOnSubmit}
                    >
                        submit
                    </Button> : null}
            </Box>
        </Dialog >
    )
}
export default AddPurchasePolicy;