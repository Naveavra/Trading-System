import React, { useEffect } from 'react';

import { Outlet, useNavigate } from 'react-router-dom';

import { styled } from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import Toolbar from '@mui/material/Toolbar';
import Box from '@mui/material/Box';
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';

import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import SettingsOutlinedIcon from '@mui/icons-material/SettingsOutlined';
import PersonOutlinedIcon from '@mui/icons-material/PersonOutlined';
import LogoutIcon from '@mui/icons-material/Logout';

import { useAppDispatch, useAppSelector } from '../redux/store';
//import { mockManagers } from '../data/mockData';
import { logout } from '../reducers/authSlice';
import SideDrawer from './Drawer';

interface TabPanelProps {
    children?: React.ReactNode;
    dir?: string;
    index: number;
    value: number;
}

const DRAWER_WIDTH = 240;

interface MyAppBarProps extends MuiAppBarProps {
    open?: boolean;
    drawerWidth: number;
}

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' && prop !== 'drawerWidth' })<{
    open?: boolean;
    drawerWidth: number;
}>(({ theme, open, drawerWidth }) => ({
    flexGrow: 1,
    padding: theme.spacing(3),
    transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    marginLeft: `-${drawerWidth}px`,
    ...(open && {
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
        marginLeft: 0,
    }),
}));

const MyAppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open' && prop !== 'drawerWidth',
})<MyAppBarProps>(({ theme, open, drawerWidth }) => ({
    transition: theme.transitions.create(['margin', 'width'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
        width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: `${drawerWidth}px`,
        transition: theme.transitions.create(['margin', 'width'], {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));


const DashboardFrame: React.FC = () => {
    //const themeColorMode = useAppSelector((state) => state.global.clientSettings.theme.colorMode);
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const navigate = useNavigate();
    const [value, setValue] = React.useState(0);
    const [open, setOpen] = React.useState(false);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const handleLogout = () => {
        dispatch(logout());
    };

    const handleSettings = () => {
        navigate('settings');
    };
    const handleProfile = () => {
        navigate('profile');
    };
    // useEffect(() => {
    //     const unloadCallback = (event: { preventDefault: () => void; returnValue: string; }) => {
    //         event.preventDefault();
    //         event.returnValue = "";
    //         return "";
    //     };

    //     window.addEventListener("beforeunload", unloadCallback);
    //     return () => window.removeEventListener("beforeunload", unloadCallback);
    // }, []);
    return (<>
        <MyAppBar
            drawerWidth={DRAWER_WIDTH}
            sx={{ position: 'sticky', width: '100%', margin: 'center', direction: 'ltr' }}

        >
            <Toolbar >
                <IconButton
                    color="inherit"
                    aria-label="open drawer"
                    onClick={handleDrawerOpen}
                    edge="start"
                    sx={{ mr: 2, ...(open && { display: 'none' }) }}
                >
                    <MenuIcon />
                </IconButton>
                <Typography position={'static'} variant="h4" component="div" sx={{ flexGrow: 2, margin: 'center', ml: 65 }}>
                    ברוך הבא לאתר {userName} שלום
                </Typography>
                <IconButton onClick={handleProfile}>
                    <NotificationsOutlinedIcon />
                </IconButton>
                <IconButton onClick={handleSettings}>
                    <SettingsOutlinedIcon />
                </IconButton>
                <IconButton onClick={handleProfile}>
                    <PersonOutlinedIcon />
                </IconButton>
                <IconButton onClick={handleLogout}>
                    <LogoutIcon color="warning" />
                </IconButton>
            </Toolbar>
        </MyAppBar>
        <Box sx={{ display: 'flex', flexGrow: 1 }}>
            <SideDrawer drawerWidth={DRAWER_WIDTH} onDrawerClose={handleDrawerClose} open={open} />
            <Main open={open} drawerWidth={DRAWER_WIDTH}>
                <Outlet />
            </Main>
        </Box>
    </>);
};

export default DashboardFrame

