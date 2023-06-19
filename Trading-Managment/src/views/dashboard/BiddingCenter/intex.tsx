import { Alert, Box, Card, CardActions, CardContent, IconButton, Tab, Tabs, Typography } from "@mui/material";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../redux/store";
import { status } from '../../../types/systemTypes/Bid'
import QuestionAnswerIcon from '@mui/icons-material/QuestionAnswer';
import WalletIcon from '@mui/icons-material/Wallet';
import { Action } from "../../../types/systemTypes/Action";
const BiddingCenter = () => {
    const [value, setValue] = useState(-1);
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const permissions = useAppSelector((state) => state.auth.permissions);
    const actions = permissions?.filter((perm) => perm.storeId == store.storeId)[0]?.actions ?? [];
    const navigate = useNavigate();
    const bids = useAppSelector((state) => state.store.storeState.watchedStore.bids);
    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);
    };
    return (
        <>
            <Bar3 headLine={"wellcome to bidding senter"} />
            <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
                <Tabs value={value} centered onChange={handleChange}>
                    <Tab label="Auctions" onClick={() => navigate(`Auctions`)} />
                    <Tab label="Lottery" onClick={() => navigate(`Lottery`)} />
                </Tabs>
            </Box>
            <Typography variant="h6" component="div" sx={{ display: 'flex', justifyContent: 'center', flexGrow: 2 }}>
                the is the bids in the store
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
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        approvers :
                                    </Typography>
                                    {bid.approved?.map((approver, index) => {
                                        return (
                                            <Typography sx={{ fontSize: 14, ml: 5 }} color="text.secondary" gutterBottom key={index}>
                                                {approver}
                                            </Typography>
                                        )
                                    })}
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                       need to approve :
                                    </Typography>
                                    {bid.notApproved?.map((approver, index) => {
                                        return (
                                            <Typography sx={{ fontSize: 14, ml: 5 }} color="text.secondary" gutterBottom key={index}>
                                                {approver}
                                            </Typography>
                                        )
                                    })}
                                    {bid.state === status.Completed ?
                                        <>
                                        <Alert severity="success">Completed</Alert>
                                        </> :
                                    bid.state === status.Approved ?
                                        <Alert severity="info">approved</Alert> :
                                        bid.state === status.Pending ?
                                            <Alert severity="info">pending</Alert> :
                                            <Alert severity="error">rejected</Alert>
                                    }                                </CardContent>
                                <CardActions>
                                    {bid.notApproved?.length != 0 &&
                                    actions.filter((action) => action === Action.updateProduct).length > 0 ?
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
    )
}
export default BiddingCenter;