import { Alert, Box, Card, CardActions, CardContent, Tab, Tabs, Typography } from "@mui/material";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../redux/store";
import { status } from '../../../types/systemTypes/Bid'

const BiddingCenter = () => {
    const [value, setValue] = useState(-1);
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
                                        bidId:  {bid.id}
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
                                        time: {bid.bidTime}
                                    </Typography>
                                    {bid.approved === status.Approved ?
                                        <Alert severity="success"></Alert> :
                                        bid.approved === status.Pending ?
                                            <Alert severity="info"></Alert> :
                                            <Alert severity="error"></Alert>
                                    }                                </CardContent>
                                <CardActions>
                                    {/* <IconButton onClick={() => handleClickOrder(order.orderId)}>
                                        <InfoIcon />
                                    </IconButton>
                                    <IconButton onClick={() => navigate(`${order.orderId}/sendComplaint`)}>
                                        <RateReviewIcon />
                                    </IconButton> */}
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