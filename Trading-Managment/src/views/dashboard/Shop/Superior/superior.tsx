import { Box, IconButton, Toolbar, Typography, styled } from "@mui/material";
import { Outlet, useNavigate, useParams } from "react-router-dom";
import SideDrawer from "../../../../components/Drawer";
import { useState } from "react";
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar';
import MenuIcon from '@mui/icons-material/Menu';
import { useAppDispatch, useAppSelector } from "../../../../redux/store";

import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import SettingsOutlinedIcon from '@mui/icons-material/SettingsOutlined';
import PersonOutlinedIcon from '@mui/icons-material/PersonOutlined';
import LogoutIcon from '@mui/icons-material/Logout';
import Bar from "../../../../components/Bars/Navbar/Navbar";
import { logout } from "../../../../reducers/authSlice";

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
const Superior: React.FC = () => {
    const params = useParams();
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const userId = useAppSelector(state => state.auth.userId);
    const userName = useAppSelector(state => state.auth.userName);
    const [open, setOpen] = useState(false);

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };
    const handleLogout = () => {
        dispatch(logout(userId));
    };

    const handleSettings = () => {
        navigate('settings');
    };
    const handleProfile = () => {
        navigate('profile');
    };
    const storeId = params.id;

    return (<>
        {/* <MyAppBar
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
                    ברוך הבא לחנות {userName} שלום
                </Typography>
                <IconButton >
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
        </MyAppBar> */}
        <Bar headLine={`ברוך הבא לחנות ${userName}`} />
        <Box sx={{ display: 'flex', flexGrow: 1 }}>
            <SideDrawer drawerWidth={DRAWER_WIDTH} onDrawerClose={handleDrawerClose} open={open} />
            <Main open={open} drawerWidth={DRAWER_WIDTH}>
                <Outlet />
            </Main>
        </Box>
    </>
    );
}


export default Superior;
