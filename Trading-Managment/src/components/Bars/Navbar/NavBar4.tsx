import { useState } from 'react';
import { logout, clearNotifications } from '../../../reducers/authSlice';
import { useAppSelector, useAppDispatch } from '../../../redux/store';
import './NavBar3.css';

import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import { Outlet, useNavigate } from 'react-router-dom';
import { Dialog, DialogTitle, DialogContent, Avatar, DialogActions, Checkbox } from '@mui/material';


import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import LogoutIcon from '@mui/icons-material/Logout';

import MenuIcon from '@mui/icons-material/Menu';
import SideDrawer from '../../SideDrawer';
import { Action } from '../../../types/systemTypes/Action';
import MessageIcon from '@mui/icons-material/Message';



interface Props {
    headLine: string;
}
const DRAWER_WIDTH = 240;
const Bar4: React.FC<Props> = ({ headLine }) => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const [openDrawer, setOpenDrawer] = useState(false);
    const [profileDialogOpen, setProfileDialogOpen] = useState(false);
    const [notificationDialogOpen, setNotificationDialogOpen] = useState(false);

    const notifications = useAppSelector((state) => state.auth.notifications);
    const userId = useAppSelector((state) => state.auth.userId);
    const userName = useAppSelector((state) => state.auth.userName);
    const isLoggedIn = useAppSelector((state) => !!state.auth.token);

    const label = { inputProps: { 'aria-label': 'Checkbox demo' } };
    const adminActions: Action[] = [Action.addAdmin, Action.closeStorePerminently, Action.seeComplaints, Action.cancelMembership];

    const handleLogout = () => {
        dispatch(logout(userId));
        navigate('/auth/login');
    };
    const handleNotification = () => {
        setNotificationDialogOpen(true);
    }
    const handleConfirm = (event: React.ChangeEvent<HTMLInputElement>, idx: number): void => {
        console.log(`confirm message ${idx}`);
    }
    const handleDrawerClose = () => {
        setOpenDrawer(false);
    }
    const handleDrawerOpen = () => {
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
                        <IconButton className="icon" sx={{ mt: 0.5 }} color="inherit" onClick={handleNotification}>
                            <div className="numberIcon">
                                <NotificationsOutlinedIcon />
                                <span>{notifications.length}</span>
                            </div>
                        </IconButton>
                        <IconButton sx={{ mt: 0.5 }} color="inherit" onClick={() => navigate('/dashboard/sendMsg')}>
                            <MessageIcon />
                        </IconButton>

                        <IconButton className="icon" color="inherit" onClick={() => {
                            setProfileDialogOpen(true);
                        }}>
                            <PersonOutlineOutlinedIcon />
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
                                    <Typography key={index} sx={{ ml: 2, mr: 3 }}>{not.content}</Typography>
                                    <Checkbox key={index} {...label} defaultChecked onChange={(e) => { handleConfirm(e, index) }} />
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
            <Box sx={{ display: 'flex', flexGrow: 1 }}>
                <SideDrawer drawerWidth={DRAWER_WIDTH} onDrawerClose={handleDrawerClose} open={openDrawer} actions={adminActions} route={"dashboard/admin"} />
            </Box>
            <Outlet />
        </>
    );
}
export default Bar4;