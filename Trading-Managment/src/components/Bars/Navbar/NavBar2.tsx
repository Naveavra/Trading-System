import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { useNavigate } from 'react-router-dom';
import { Dialog, DialogTitle, DialogContent, Avatar, DialogActions } from '@mui/material';
import { logout, guestEnter } from '../../../reducers/authSlice';
import { getStore } from '../../../reducers/storesSlice';
import { useAppSelector, useAppDispatch } from '../../../redux/store';
import './NavBar2.css';
import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import StorefrontIcon from '@mui/icons-material/Storefront';
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import LogoutIcon from '@mui/icons-material/Logout';
import SideDrawer from '../../SideDrawer';

interface Props {
    headLine: string;
}
const DRAWER_WIDTH = 240;
const Bar2: React.FC<Props> = ({ headLine }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const [storeOpen, setStoreOpen] = React.useState(false);
    const [openDrawer, setOpenDrawer] = React.useState(false);
    const [profileDialogOpen, setProfileDialogOpen] = React.useState(false);
    const products = [];// = useSelector((state) => state.cart.products);
    const notifications = [];// = useSelector((state) => state.auth.notifications);
    const isLoggedIn = useAppSelector((state) => !!state.auth.token);
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const stores = [
        { number: 1, name: 'nike', role: 'manager', src: 'https://images.pexels.com/photos/8176112/pexels-photo-8176112.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1' },
        { number: 2, name: 'apple', role: 'creator', src: 'https://images.pexels.com/photos/13748756/pexels-photo-13748756.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1' }
    ]//useAppSelector((state) => state.auth.storeRoles);


    const handleLogout = () => {
        console.log("logout");
        dispatch(logout(userId));
        dispatch(guestEnter());
        navigate('/dashboard');
    };
    const handleChooseStore = (storeNumber: number) => () => {
        console.log("choose store", storeNumber);
        dispatch(getStore({ userId: userId, storeId: storeNumber }));
        navigate('shops/superior');
    }
    const handleDrawerClose = () => {
        console.log("open drawer");
        setOpenDrawer(false);
    }
    const handleDrawerOpen = () => {
        console.log("close drawer");
        setOpenDrawer(true);
    }
    return (
        <>
            <Box sx={{ flexGrow: 1 }}>
                <AppBar position="static">
                    <Toolbar>
                        <IconButton
                            size="large"
                            edge="start"
                            color="inherit"
                            aria-label="menu"
                            className="icon"
                            onClick={handleDrawerOpen}
                            sx={{ mr: 2, ...(openDrawer && { display: 'none' }) }}
                        >
                            <MenuIcon />
                        </IconButton>

                        <Typography variant="h6" component="div" sx={{ flexGrow: 2, ml: 73 }}>
                            {headLine}
                        </Typography>
                        <IconButton className="icon" color="inherit" onClick={handleLogout}>
                            <LogoutIcon />
                        </IconButton>
                        <IconButton className="icon" color="inherit" onClick={() => setStoreOpen(true)}>
                            <StorefrontIcon />
                        </IconButton>
                        <IconButton className="icon" sx={{ mt: 0.5 }} color="inherit" onClick={() => { console.log("notifications") }}>
                            <div className="numberIcon">
                                <NotificationsOutlinedIcon />
                                <span>{products.length}</span>
                            </div>
                        </IconButton>
                        <IconButton className="icon" color="inherit" onClick={() => {
                            setProfileDialogOpen(true);
                        }}>
                            <PersonOutlineOutlinedIcon />
                        </IconButton>
                        <IconButton className="icon" sx={{ mt: 0.5 }} color="inherit" onClick={() => navigate(`/dashboard/${userId}/cart`)}>
                            <div className="numberIcon">
                                <ShoppingCartOutlinedIcon />
                                <span>{products.length}</span>
                            </div>
                        </IconButton>
                    </Toolbar>
                </AppBar>
            </Box >
            <Dialog
                open={profileDialogOpen}
                onClose={() => {
                    setProfileDialogOpen(false);
                }}
            >
                <DialogTitle>Profile</DialogTitle>
                <DialogContent dividers>
                    <Box display="flex" alignItems="center">
                        <Avatar />
                        <Box ml={3}>
                            <Typography>{userName}</Typography>
                        </Box>
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => {
                            setProfileDialogOpen(false);
                            dispatch(logout(userId));
                            navigate('/auth/login')
                        }}
                    >
                        logout
                    </Button>
                    <Button
                        onClick={() => {
                            setProfileDialogOpen(false);
                        }}
                    >
                        Cancel
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                open={storeOpen}
                onClose={() => {
                    setStoreOpen(false);
                }}
            >
                <DialogTitle>your stores</DialogTitle>
                <>
                    {stores.map((store) => {
                        return (
                            <DialogContent dividers key={store.name}>
                                <Button onClick={handleChooseStore(store.number)}>
                                    <Avatar src={store.src} />
                                    <Box ml={3} display={'flex'} >
                                        <Typography sx={{ ml: 2, mr: 3 }}>{store.name}</Typography>
                                        <Typography>{store.role}</Typography>
                                    </Box>
                                </Button>
                            </DialogContent>
                        )
                    }
                    )}
                </>
                <DialogActions>
                    <Button
                        onClick={() => {
                            setStoreOpen(false);
                        }}
                    >
                        Cancel
                    </Button>
                </DialogActions>
            </Dialog>
            <Box sx={{ display: 'flex', flexGrow: 1 }}>
                <SideDrawer drawerWidth={DRAWER_WIDTH} onDrawerClose={handleDrawerClose} open={openDrawer} />
            </Box>
        </>
    );
}
export default Bar2;