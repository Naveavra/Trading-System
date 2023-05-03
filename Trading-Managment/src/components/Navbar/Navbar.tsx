import React, { useState } from "react";
import NotificationsOutlinedIcon from '@mui/icons-material/NotificationsOutlined';
import StorefrontIcon from '@mui/icons-material/Storefront';
import PersonOutlineOutlinedIcon from "@mui/icons-material/PersonOutlineOutlined";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import { Link } from "react-router-dom";
import "./Navbar.css"
import LogoutIcon from '@mui/icons-material/Logout';

import Cart from "../Cart/Cart";
import { Product } from "../../types/systemTypes/Product";
import { IconButton, Typography } from "@mui/material";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { logout } from "../../reducers/authSlice";

const Bar = () => {
    const [open, setOpen] = useState(false)
    const products: Product[] = [];// = useSelector((state) => state.cart.products);
    const isLoggedIn = useAppSelector((state) => !!state.auth.token);
    const dispatch = useAppDispatch();
    const handleLogout = () => {
        console.log("logout");
        dispatch(logout());
    };

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
                        Trading System
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
                        <IconButton onClick={() => { console.log("notifications") }}>
                            <NotificationsOutlinedIcon />
                        </IconButton>
                        <IconButton onClick={() => { console.log("details") }}>
                            <PersonOutlineOutlinedIcon />
                        </IconButton>
                        {isLoggedIn &&
                            <IconButton onClick={handleLogout}>
                                <LogoutIcon />
                            </IconButton>
                        }
                        {isLoggedIn &&
                            <IconButton onClick={() => { console.log("store") }}>
                                <StorefrontIcon />
                            </IconButton>}
                        <IconButton onClick={() => setOpen(!open)}>
                            <div className="cartIcon">
                                <ShoppingCartOutlinedIcon />
                                <span>{products.length}</span>
                            </div>
                        </IconButton>

                    </div>
                </div>
            </div>
            {/* {open && <Cart />} */}
        </div>
    );
};

export default Bar;