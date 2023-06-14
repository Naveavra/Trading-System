import { Box, Tab, Tabs, Typography } from "@mui/material";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const BiddingCenter = () => {
    const [value, setValue] = useState(-1);
    const navigate = useNavigate();

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
        </>
    )
}
export default BiddingCenter;