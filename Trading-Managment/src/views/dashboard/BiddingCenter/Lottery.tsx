import { Box, Tab, Tabs } from "@mui/material";
import Bar3 from "../../../components/Bars/Navbar/NavBar3";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Lottery = () => {
    const [value, setValue] = useState(0);
    const navigate = useNavigate();

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setValue(newValue);

    };
    return (
        <>
            <Bar3 headLine={"wellcome to bidding senter"} />
            <Box sx={{ width: '100%', bgcolor: 'background.paper' }}>
                <Tabs value={value} centered onChange={handleChange}>
                    <Tab label="Ouctions" onClick={() => navigate(`Ouctions`)} />
                    <Tab label="Lottery" onClick={() => navigate(`Lottery`)} />
                </Tabs>

            </Box>
        </>
    )
}
export default Lottery;