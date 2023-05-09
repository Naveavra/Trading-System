import * as React from 'react';
import { styled, useTheme } from '@mui/material/styles';
import MuiDrawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import Synagogue from '@mui/icons-material/Synagogue';
import History from '@mui/icons-material/History';
import Inventory from '@mui/icons-material/Inventory';
import InsertDriveFile from '@mui/icons-material/InsertDriveFile';
import SupportAgent from '@mui/icons-material/SupportAgent';
import ImportContacts from '@mui/icons-material/ImportContacts';
import { useParams, useNavigate } from 'react-router-dom';
import Settings from '@mui/icons-material/Settings';
import PaymentIcon from '@mui/icons-material/Payment';
import PeopleIcon from '@mui/icons-material/People';
import InfoIcon from '@mui/icons-material/Info';
import { SvgIconProps } from '@mui/material';

interface item {
    text: string,
    item: JSX.Element,
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

const SideDrawer: React.FC<SideDrawerProps> = ({ onDrawerClose, drawerWidth, open }) => {
    const theme = useTheme();
    const history = useParams();
    const navigate = useNavigate();
    const categories: item[] = [
        {
            text: 'מתפללים',
            item: <PeopleIcon />,
            onclick: () => { navigate('/dashboard/customers') }
        },
        {
            text: 'תרומות',
            item: <PaymentIcon />,
            onclick: () => { navigate('/dashboard/payments') }
        },
        {
            text: 'כללי',
            item: <InfoIcon />,
            onclick: () => { navigate('/dashboard/general') }
        },
    ]
    const itemList = [
        {
            text: 'ארועים',
            item: <Synagogue />,
            onclick: () => { navigate('/dashboard/events'); }
        },
        {
            text: 'טפסים',
            item: <InsertDriveFile />,
            onclick: () => { navigate('/dashboard/forms'); }
        },
        {
            text: 'היסטוריה',
            item: <History />,
            onclick: () => { navigate('/dashboard/history'); }
        },
        {
            text: 'הודעות נכנסות',
            item: <InboxIcon />,
            onclick: () => { navigate('/dashboard/inbox'); }
        },
        {
            text: 'איחסון',
            item: <Inventory />,
            onclick: () => { navigate('/dashboard/storage'); }
        },
    ];
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
                {categories.map((icon) => {
                    const { text, item, onclick } = icon;
                    return (
                        <ListItem button key={text} onClick={onclick}>
                            {item && <ListItemIcon>{item}</ListItemIcon>}
                            <ListItemText primary={text} />
                        </ListItem>
                    );
                })}
            </List>
            <Divider />
            <List>
                {itemList.map((icon) => {
                    const { text, item, onclick } = icon;
                    return (
                        <ListItem button key={text} onClick={onclick}>
                            {item && <ListItemIcon>{item}</ListItemIcon>}
                            <ListItemText primary={text} />
                        </ListItem>
                    );
                })}
            </List>
            <Divider />
            <List>
                <List>
                    {helpList.map((icon) => {
                        const { text, item, onclick } = icon;
                        return (
                            <ListItem button key={text} onClick={onclick}>
                                {item && <ListItemIcon>{item}</ListItemIcon>}
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