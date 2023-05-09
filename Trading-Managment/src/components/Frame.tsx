import React, { useEffect, useState } from 'react';

import { Outlet, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../redux/store';
//import { mockManagers } from '../data/mockData';
//import  Navebar  from '../components/NavBar/Navbar';
import Bar from './Bars/Navbar/Navbar';
import { SearchBar } from './Bars/SearchBar/SearchBar';
import Categories2 from './Categories/category2';
import ShopsBar from './Bars/ShopBar/ShopBar';
import { Divider } from '@mui/material';
import axios from 'axios';

const DashboardFrame: React.FC = () => {

    //const themeColorMode = useAppSelector((state) => state.global.clientSettings.theme.colorMode);
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const navigate = useNavigate();
    const [value, setValue] = React.useState(0);
    const [open, setOpen] = React.useState(false);
    const [query, setQuery] = useState("");
    const PING_INTERVAL = 10000; // 10 seconds in milliseconds
    useEffect(() => {
        // dispatch(getShops({}));
        // dispatch(getProducts({ category: 'all' }));
    }, [dispatch])
    // Send a ping to the server
    const sendPing = () => {
        console.log("pinging", userId);
        if (userId != 0) {
            console.log("ping", userId);
            axios.post('http://localhost:4567/api/auth/ping', { userId: userId })
                .then(response => {
                    // Do something with the response if necessary
                })
                .catch(error => {
                    // Handle the error if necessary
                });
            // dispatch(ping(userId));
        }
    }
    useEffect(() => {
        // Call the sendPing function every 2 seconds
        console.log("mount");
        const pingInterval = setInterval(sendPing, PING_INTERVAL);

        // Stop the ping interval when the user leaves the app
        return () => {
            console.log("unmount");
            clearInterval(pingInterval)
        };

    }, [])
    return (<>
        <Bar headLine={"Trading System"} />
        <SearchBar />
        <Divider sx={{ marginTop: 1 }} />
        <ShopsBar />
        <Divider />
        {/* <Categories /> */}
        <Categories2 />
        {/* <Products /> */}
        <Outlet />

    </>);
};

export default DashboardFrame

