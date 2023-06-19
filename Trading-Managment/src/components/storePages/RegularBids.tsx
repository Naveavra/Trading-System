import { Box, Tabs, Tab, Typography, Card, CardContent, Alert, CardActions, IconButton } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { Action } from "../../types/systemTypes/Action";
import Bar3 from "../Bars/Navbar/NavBar3";
import { status } from '../../types/systemTypes/Bid';
import QuestionAnswerIcon from '@mui/icons-material/QuestionAnswer';
import WalletIcon from '@mui/icons-material/Wallet';
import { useEffect } from "react";
import { getStore } from "../../reducers/storesSlice";
const RegularBids = () => {
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const permissions = useAppSelector((state) => state.auth.permissions);
    const actions = permissions?.filter((perm) => perm.storeId == store.storeId)[0]?.actions ?? [];
    const navigate = useNavigate();
    const bids = useAppSelector((state) => state.store.storeState.watchedStore.bids);
    useEffect(() => {
        dispatch(getStore({ userId: userId, storeId: store.storeId }));
    }, []);

    return (
        <>
            <Bar3 headLine={"wellcome to bidding senter"} />
            <Typography variant="h6" component="div" sx={{ display: 'flex', justifyContent: 'center', flexGrow: 2 }}>
                the is the regular bids in the store
            </Typography>
            <Box sx={{ display: "flex", width: '100%', mb: 2 }}>
                {
                    bids?.map((bid, index) => {
                        return (
                            <Card sx={{ width: 200, mt: 5, ml: 3 }} key={index}>
                                <CardContent>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        bidId:  {bid.bidId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        storeId:  {bid.storeId}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        product: {bid.product.name}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        current offer: {String(bid.offer)}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        product price: {bid.product.price}
                                    </Typography>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        time: {bid.time}
                                    </Typography>
                                    {bid.state === status.Approved ?
                                        <Alert severity="success">approved</Alert> :
                                        bid.state === status.Pending ?
                                            <Alert severity="info">pending</Alert> :
                                             bid.state === status.Counter ?
                                            <>
                                                <Alert severity="info">counter</Alert>
                                            </> :
                                            <Alert severity="error">rejected</Alert>
                                    }                                </CardContent>
                                <CardActions>
                                    {actions.filter((action) => action === Action.updateProduct).length > 0 ?
                                        <>
                                            <IconButton onClick={() => navigate(`${store.storeId}/${bid.product.productId}/${bid.bidId}/answerBid`)}>
                                                <QuestionAnswerIcon />
                                            </IconButton>
                                            <IconButton onClick={() => navigate(`${store.storeId}/${bid.product.productId}/${bid.bidId}/counterBid`)}>
                                                <WalletIcon />
                                            </IconButton>
                                        </>
                                        : null}
                                </CardActions>
                            </Card>
                        )
                    })
                }
            </Box>
        </>
    );
}
export default RegularBids;