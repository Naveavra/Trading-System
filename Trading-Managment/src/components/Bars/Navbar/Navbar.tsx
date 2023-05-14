import React, { useEffect, useState } from "react";
import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import StorefrontIcon from '@mui/icons-material/Storefront';
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css"
import LogoutIcon from '@mui/icons-material/Logout';
import PersonIcon from '@mui/icons-material/Person';
import AddIcon from '@mui/icons-material/Add';
import Cart from "../../Cart/Cart";
import { Product } from "../../../types/systemTypes/Product";
import { Avatar, Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, List, ListItem, ListItemAvatar, ListItemButton, ListItemText, MenuItem, Typography } from "@mui/material";
import { useAppDispatch, useAppSelector } from "../../../redux/store";
import { guestEnter, logout } from "../../../reducers/authSlice";
import { Server as WebSocketServer, get } from 'http';
import { useSocket } from "../../../hooks/useSocket";
import { blue } from "@mui/material/colors";
import { getStore } from "../../../reducers/storesSlice";

interface Props {
    headLine: string;
}
const Bar: React.FC<Props> = ({ headLine }) => {

    const [storeOpen, setStoreOpen] = useState(false)
    const [profileDialogOpen, setProfileDialogOpen] = useState(false);
    const products: Product[] = [];// = useSelector((state) => state.cart.products);
    const isLoggedIn = useAppSelector((state) => !!state.auth.token);
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const stores = [{ number: 1, name: 'nike', role: 'manager', src: 'https://images.pexels.com/photos/8176112/pexels-photo-8176112.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1' },
    { number: 2, name: 'apple', role: 'creator', src: 'https://images.pexels.com/photos/13748756/pexels-photo-13748756.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1' }]//useAppSelector((state) => state.auth.storeRoles);
    const navigate = useNavigate();
    const dispatch = useAppDispatch();


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


    return (
        <div className="navbar">
            <div className="wrapper">
                {/* <div className="left">

                    <div className="item">
                        <Link className="link" to="/products/1">Women</Link>
                    </div>
                    <div className="item">
                        <Link className="link" to="/products/2">Men</Link>
                    </div>
                    <div className="item">
                        <Link className="link" to="/products/3">Children</Link>
                    </div>
                </div> */}
                <div className="center">
                    <Typography variant="h4" component="h4" className="logo" fontFamily={"Gill Sans"}>
                        {headLine}
                    </Typography>
                </div>
                <div className="right">

                    <div className="item">
                        <Link className="link" to="/">About</Link>
                    </div>
                    <div className="item">
                        <Link className="link" to="/">Contact</Link>
                    </div>

                    <div className="icons">
                        {isLoggedIn &&
                            <>
                                <IconButton onClick={handleLogout}>
                                    <LogoutIcon />
                                </IconButton>
                                <IconButton onClick={() => setStoreOpen(true)}>
                                    <StorefrontIcon />
                                </IconButton>
                                <IconButton onClick={() => { console.log("notifications") }}>
                                    <NotificationsOutlinedIcon />
                                </IconButton>
                                <IconButton onClick={() => {
                                    setProfileDialogOpen(true);
                                }}>
                                    <PersonOutlineOutlinedIcon />
                                </IconButton>
                            </>
                        }
                        {!isLoggedIn &&
                            <>
                                <IconButton onClick={() => {
                                    navigate('/auth/login');
                                }}>
                                    <PersonOutlineOutlinedIcon />
                                </IconButton>
                            </>
                        }
                        <IconButton onClick={() => navigate(`/dashboard/${userId}/cart`)}>
                            <div className="cartIcon">
                                <ShoppingCartOutlinedIcon />
                                <span>{products.length}</span>
                            </div>
                        </IconButton>


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
                                        <DialogContent dividers>
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

                    </div>
                </div>
            </div>
            {/* {open && <Cart />} */}
        </div >
    );
};
export interface SimpleDialogProps {
    open: boolean;
    onClose: (value: string) => void;
}


export default Bar;