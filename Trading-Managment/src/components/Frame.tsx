import React, { useState } from 'react';

import { useNavigate } from 'react-router-dom';
import { styled } from '@mui/material/styles';
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import { useAppDispatch, useAppSelector } from '../redux/store';
//import { mockManagers } from '../data/mockData';
import { logout } from '../reducers/authSlice';
//import  Navebar  from '../components/NavBar/Navbar';
import Bar from './Navbar/Navbar';
import { SearchBar } from './SearchBar/SearchBar';
import Categories from './Categories/categories';
import Categories2 from './Categories/category2';






const DashboardFrame: React.FC = () => {
    const [input, setInput] = useState("");
    //const themeColorMode = useAppSelector((state) => state.global.clientSettings.theme.colorMode);
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const navigate = useNavigate();
    const [value, setValue] = React.useState(0);
    const [open, setOpen] = React.useState(false);
    const [query, setQuery] = useState("");


    return (<>
        <Bar />
        <SearchBar />
        {/* <Categories /> */}
        <Categories2 />
    </>);
};

export default DashboardFrame

