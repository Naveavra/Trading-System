import { Dialog, Box, Grid, Typography, Button, TextField } from "@mui/material";
import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { RootState, useAppDispatch, useAppSelector } from "../../../redux/store";
import { addFirstComposite, addSecondComposite, setpercentageToRegularDiscount } from "../../../reducers/discountSlice";

interface CompositeDiscountProps {
    first: boolean;
}
const CompositeDiscount: React.FC<CompositeDiscountProps> = ({ first }) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const [rootSource, setRootSource] = useState('');
    const [percentageInput, setPercentageInput] = useState('');
    const [error, setError] = useState('');
    const [type, setType] = useState('');
    const [composoreType, setComposoreType] = useState('');
    const [numericType, setNumericType] = useState('');
    const [xorRule, setXorRule] = useState('');


    const handleOnClose = useCallback(() => {
        navigate(-1);
        //dispatch(getStore({ userId: userId, storeId: storeId }));
    }, []);
    const handleSetSource = (input: string) => {
        setRootSource(input);
    }
    const handleSetpercentage = (input: string) => {
        const percentage = parseFloat(input);
        if (percentage > 100 || percentage < 0) {
            setError('percentage must be between 0 to 100');
        }
        else {
            setError('');
            console.log(percentage);
            setPercentageInput(input);
        }
    }
    const handleOnLogical = () => {
        setType('Logical');
    }
    const handleOnNumeric = () => {
        setType('Numeric');
    }

    const handleOnSubmit = () => {
        debugger;
        if (first) {
            dispatch(addFirstComposite({
                percentage: percentageInput,
                type: type,
                composoreType: composoreType,
                numericType: numericType,
                xorRule: xorRule,
            }));
        }
        else {
            dispatch(addSecondComposite({
                rootSource: rootSource,
                percentage: percentageInput,
                type: type,
                composoreType: composoreType,
                numericType: numericType,
                xorRule: xorRule,
            }));
        }
        navigate("/dashboard/store/superior/conditionalDiscount/leafs");
    };
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
                        enter details
                    </Typography>
                </Grid>
                {!first ?
                    <Grid item xs={12}>
                        <TextField
                            required
                            id="outlined-required"
                            label="root source"
                            error={error != ''}
                            onChange={(e) => { handleSetSource(e.target.value) }}
                        />
                    </Grid> : null
                }
                <Grid item xs={12} sx={{ mt: 2 }}>
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
                        onClick={handleOnLogical}
                        color={type === 'Logical' ? 'success' : 'primary'}
                    >
                        Logical
                    </Button>
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                        onClick={handleOnNumeric}
                        color={type === 'Numeric' ? 'success' : 'primary'}
                    >
                        Numeric
                    </Button>
                </Box>
                {type != '' && type === 'Logical' ?
                    <>
                        <Box display={'flex'}>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => setComposoreType('AND')}
                                color={composoreType === 'AND' ? 'success' : 'primary'}
                            >
                                AND
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => setComposoreType('OR')}
                                color={composoreType === 'OR' ? 'success' : 'primary'}
                            >
                                OR
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => setComposoreType('XOR')}
                                color={composoreType === 'XOR' ? 'success' : 'primary'}
                            >
                                XOR
                            </Button>
                        </Box>
                        {composoreType === 'XOR' ?
                            <Box display={'flex'}>
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                    onClick={() => setXorRule('MaxDiscountValue')}
                                    color={xorRule === 'MaxDiscountValue' ? 'success' : 'primary'}
                                >
                                    MaxDiscountValue
                                </Button>
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                    onClick={() => setXorRule('MinDiscountValue')}
                                    color={xorRule === 'MinDiscountValue' ? 'success' : 'primary'}
                                >
                                    MinDiscountValue
                                </Button>
                            </Box> :
                            null}
                    </>
                    : null}
                {type != '' && type === 'Numeric' ?
                    <>
                        <Box display={'flex'}>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => setNumericType('MAX')}
                                color={numericType === 'MAX' ? 'success' : 'primary'}
                            >
                                MAX
                            </Button>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                                onClick={() => setNumericType('ADDITTION')}
                                color={numericType === 'ADDITTION' ? 'success' : 'primary'}
                            >
                                ADDITTION
                            </Button>
                        </Box>

                    </>
                    : null
                }
                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{ mt: 3, mb: 2, marginRight: 2, marginLeft: 2 }}
                    onClick={handleOnSubmit}
                >
                    submit
                </Button>
            </Box>
        </Dialog >
    );
}
export default CompositeDiscount;