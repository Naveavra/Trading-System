import React from "react";
import "./Cart.css";
import DeleteOutlinedIcon from "@mui/icons-material/DeleteOutlined";
//import { removeItem, resetCart } from "../../redux/cartReducer";
;
//import { makeRequest } from "../../makeRequest";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { Product } from "../../types/systemTypes/Product";
import { CartProduct } from "../../types/systemTypes/CartProduct";
import { buyCart, removeFromCart } from "../../reducers/cartSlice";
import ProductCard from "../ProductInStore/Card";
import { Button, Card, Typography } from "@mui/material";
import ProductInCart from "../ProductInCart/ProductCart";

const Cart = () => {
    //todo: implemet redux cart functionality
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);
    const cart = useAppSelector((state) => state.cart.responseData);
    const products = cart?.flat();
    const totalPrice = products?.reduce((acc, product) => acc + product.quantity * product.price, 0);



    const handlePayment = async () => {
        //dispatch(handshake());
        //dispatch(pay)
        //dispatch(cleancrt)
    };
    return (
        <>
            <Typography variant="h4">Cart</Typography>
            <Typography variant="h6">Total: {totalPrice}</Typography>
            <Button onClick={handlePayment}>Pay</Button>
            {products?.map((product) => (
                <ProductInCart item={product} />
            )
            )}
        </>
    )
};

export default Cart;