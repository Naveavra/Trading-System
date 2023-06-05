import * as React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { logout, clearNotifications } from '../../../reducers/authSlice';
import { getStore } from '../../../reducers/storesSlice';
import { useAppSelector, useAppDispatch } from '../../../redux/store';
import './NavBar3.css';
import { StoreRole } from '../../../types/systemTypes/StoreRole';

import { Dialog, DialogTitle, DialogContent, Avatar, DialogActions, Checkbox } from '@mui/material';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import StorefrontIcon from '@mui/icons-material/Storefront';
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import LogoutIcon from '@mui/icons-material/Logout';
import MessageIcon from '@mui/icons-material/Message';
import RateReviewIcon from '@mui/icons-material/RateReview';
import HomeIcon from '@mui/icons-material/Home';


interface Props {
    headLine: string;
}
const Bar3: React.FC<Props> = ({ headLine }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const [storeOpen, setStoreOpen] = useState(false);
    const [profileDialogOpen, setProfileDialogOpen] = useState(false);
    const [notificationDialogOpen, setNotificationDialogOpen] = useState(false);

    const notifications = useAppSelector((state) => state.auth.notifications);
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const isLoggedIn = useAppSelector((state) => !!state.auth.token);

    const cart = useAppSelector((state) => state.cart.responseData);
    const numProductsIncart = cart?.reduce((acc, item) => acc + item.quantity, 0) ?? 0;

    const stores_roles: StoreRole[] = useAppSelector((state) => state.auth.storeRoles);
    const stores_names = useAppSelector((state) => state.auth.storeNames);
    const store_images = useAppSelector((state) => state.auth.storeImgs);
    const stores = stores_roles ? stores_roles.map((role, index) => {
        return {
            storeId: role.storeId,
            storeRole: role.storeRole,
            storeName: stores_names[index].storeName,
            storeImg: store_images[index].storeImg,
        }
    }) : [];


    const storeInfo = useAppSelector((state) => state.store.storeState.wahtchedStoreInfo);
    const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

    const handleLogout = () => {
        dispatch(logout(userId));
        navigate('/auth/login');
    };
    const handleChooseStore = (storeNumber: number) => () => {
        dispatch(getStore({ userId: userId, storeId: storeNumber }));
        navigate('/dashboard/store/superior');
        setStoreOpen(false);
    }

    const handleNotification = () => {
        setNotificationDialogOpen(true);
    }
    const handleConfirm = (event: React.ChangeEvent<HTMLInputElement>, idx: number): void => {
        console.log(`confirm message ${idx}`);
    }
    // useEffect(() => {
    //     if (token != '') {
    //         dispatch(getNotifications({ userId: userId, token: token }));
    //     }

    // }, [dispatch])

    return (
        <>
            <Box sx={{ flexGrow: 1 }}>
                <AppBar position="static">
                    <Toolbar>


                        <Typography variant="h6" component="div" sx={{ flexGrow: 2, ml: 73 }}>
                            {headLine}
                        </Typography>
                        <IconButton className="icon" color="inherit" onClick={() => navigate(`/dashboard`)}>
                            <HomeIcon />
                        </IconButton>
                        {isLoggedIn &&
                            <>

                                <IconButton className="icon" color="inherit" onClick={handleLogout}>
                                    <LogoutIcon />
                                </IconButton>
                                <IconButton className="icon" color="inherit" onClick={() => setStoreOpen(true)}>
                                    <StorefrontIcon />
                                </IconButton>
                                <IconButton className="icon" sx={{ mt: 0.5 }} color="inherit" onClick={handleNotification}>
                                    <div className="numberIcon">
                                        <NotificationsOutlinedIcon />
                                        <span>{notifications.length}</span>
                                    </div>
                                </IconButton>
                            </>
                        }
                        <IconButton className="icon" color="inherit" onClick={() => {
                            setProfileDialogOpen(true);
                        }}>
                            <PersonOutlineOutlinedIcon />
                        </IconButton>
                        <IconButton className="icon" sx={{ mt: 0.5 }} color="inherit" onClick={() => navigate(`/dashboard/cart`)}>
                            <div className="numberIcon">
                                <ShoppingCartOutlinedIcon />
                                {/* {need to sum up products quantity in every basket} */}
                                <span>{numProductsIncart}</span>
                            </div>
                        </IconButton>
                        {/* {isLoggedIn ?
                            <>
                                <IconButton sx={{ mt: 0.5 }} color="inherit" onClick={() => navigate('/dashboard/sendMsg')}>
                                    <MessageIcon />
                                </IconButton>
                                <IconButton sx={{ mt: 0.5 }} color="inherit" onClick={() => navigate('/dashboard/sendComplaint')}>
                                    <RateReviewIcon />
                                </IconButton>
                            </> : null
                        } */}
                    </Toolbar>
                </AppBar>
            </Box >
            <Dialog
                open={profileDialogOpen}
                onClose={() => {
                    setProfileDialogOpen(false);
                }}
            >
                {isLoggedIn ?
                    <>
                        <DialogTitle>Profile</DialogTitle>
                        <DialogContent dividers>
                            <Box display="flex" alignItems="center" onClick={() => { navigate('/dashboard/personal') }}>
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
                    </> :
                    <DialogActions>
                        <Button
                            onClick={() => {
                                setProfileDialogOpen(false);
                                navigate('/auth/login')
                            }}
                        >
                            login
                        </Button>
                        <Button
                            onClick={() => {
                                setProfileDialogOpen(false);
                            }}
                        >
                            Cancel
                        </Button>
                    </DialogActions>
                }
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
                            <DialogContent dividers key={store.storeId}>
                                <Button onClick={handleChooseStore(store.storeId)}>
                                    <Avatar src={store.storeImg} />
                                    <Box ml={3} display={'flex'} >
                                        <Typography sx={{ ml: 2, mr: 3 }}>{store.storeName}</Typography>
                                        <Typography>{store.storeRole}</Typography>
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
                    <Button
                        onClick={() => {
                            setStoreOpen(false);
                            navigate('/dashboard/store/new');
                        }}
                    >
                        add new store
                    </Button>
                </DialogActions>
            </Dialog>
            {/*--------------notofications-----------*/}
            {/* -------------------------------------notofication---------------- */}
            <Dialog
                open={notificationDialogOpen}
                onClose={() => {
                    setNotificationDialogOpen(false);
                }}
            >
                <DialogTitle>your notifications</DialogTitle>
                <>
                    {notifications?.map((not, index) => {
                        return (
                            <DialogContent dividers key={index}>
                                <Box ml={3} display={'flex'} key={index}>
                                    <Typography sx={{ ml: 2, mr: 3 }}>{not.content}</Typography>
                                    <Checkbox {...label} defaultChecked onChange={(e) => { handleConfirm(e, index) }} />
                                </Box>
                            </DialogContent>
                        )
                    }
                    )}
                </>
                <DialogActions>
                    <Button
                        onClick={() => {
                            setNotificationDialogOpen(false);
                            dispatch(clearNotifications());
                        }}
                    >
                        close
                    </Button>
                </DialogActions>
            </Dialog>

        </>
    );
}
export default Bar3;