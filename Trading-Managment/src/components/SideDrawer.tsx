import * as React from 'react';
import { styled, useTheme } from '@mui/material/styles';
import MuiDrawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import MailIcon from '@mui/icons-material/Mail';
import SupportAgent from '@mui/icons-material/SupportAgent';
import ImportContacts from '@mui/icons-material/ImportContacts';
import { useParams, useNavigate } from 'react-router-dom';
import Settings from '@mui/icons-material/Settings';
import { RootState, useAppSelector } from '../redux/store';
import { Action } from '../types/systemTypes/Action';

interface item {
    text: Action,
    onclick: () => void
}
const DrawerHeader = styled('div')(({ theme }) => ({
    display: 'flex',
    alignItems: 'center',
    padding: theme.spacing(0, 1),
    // necessary for content to be below app bar
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
}));

interface SideDrawerProps {
    onDrawerClose: () => void;
    drawerWidth: number;
    open: boolean;
};

const itemsMap: Record<string, item> = {};

const SideDrawer: React.FC<SideDrawerProps> = ({ onDrawerClose, drawerWidth, open }) => {
    const theme = useTheme();
    const navigate = useNavigate();
    const store = useAppSelector((state) => state.store.storeState.watchedStore);
    const permissions = useAppSelector((state: RootState) => state.auth.permissions);
    const actions = permissions.filter((perm) => perm.storeId == store.id)[0].actions;
    for (const a of actions) {
        itemsMap[a] = {
            text: a,
            onclick: () => { navigate(`/dashboard/shops/superior/${a.replace(/\s/g, "")}`) }
        }
    }
    const actinosList: item[] = actions.map((action) => {
        return (
            {
                text: action,
                onclick: itemsMap[action].onclick
            }
        )
    })

    const helpList = [
        {
            text: 'הגדרות',
            item: <Settings />,
            onclick: () => { navigate('/dashboard/settings'); }
        },
        {
            text: 'מדריך משתמש',
            item: <ImportContacts />,
            onclick: () => { navigate('/dashboard/guide'); }
        },
        {
            text: 'צור קשר',
            item: <SupportAgent />,
            onclick: () => { navigate('/dashboard/contact'); }
        },
        {
            text: 'עלינו',
            item: <MailIcon />,
            onclick: () => { navigate('/dashboard/about'); }
        }
    ]
    return (
        <MuiDrawer
            sx={{
                direction: 'ltr',
                width: drawerWidth,
                flexShrink: 0,
                '& .MuiDrawer-paper': {
                    width: drawerWidth,
                    boxSizing: 'border-box',
                    scrollbarColor: 'rgba(0, 0, 0, 0.5) rgba(0, 0, 0, 0.1)',
                    scrollbarWidth: 'thin',
                },
            }}
            variant="persistent"
            anchor="left"
            open={open}
            onClick={onDrawerClose}
        >
            <DrawerHeader>
                <IconButton onClick={onDrawerClose}>
                    {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
                </IconButton>
            </DrawerHeader>
            <List>
                {actinosList.map((icon) => {
                    const { text, onclick } = icon;
                    return (
                        <ListItem button key={text} onClick={onclick}>
                            <ListItemText primary={text} />
                        </ListItem>
                    );
                })}
            </List>
            <Divider />
            <List>
                <List>
                    {helpList.map((icon) => {
                        const { text, onclick } = icon;
                        return (
                            <ListItem button key={text} onClick={onclick}>
                                <ListItemText primary={text} />
                            </ListItem>
                        );
                    })}
                </List>
            </List>
        </MuiDrawer>
    );
}

export default SideDrawer;