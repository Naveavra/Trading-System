import "./Cart.css";
//import { removeItem, resetCart } from "../../redux/cartReducer";
;
//import { makeRequest } from "../../makeRequest";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { Button, Typography } from "@mui/material";
import ProductInCart from "../ProductInCart/ProductCart";
import { useNavigate } from "react-router-dom";
import Bar3 from "../Bars/Navbar/NavBar3";
import { useEffect } from "react";
import { clearCartError, clearCartMessage, clearCartsError, getCart } from "../../reducers/cartSlice";
import { clearStoreError } from "../../reducers/storesSlice";
import ErrorAlert from "../Alerts/error";
import SuccessAlert from "../Alerts/success";

const Cart = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const userId = useAppSelector((state) => state.auth.userId);
    const cart = useAppSelector((state) => state.cart.responseData);
    const products = cart?.flat();
    const systemProducts = useAppSelector((state) => state.product.responseData);
    const totalPrice = products?.reduce((acc, product) => acc + product.quantity * product.price, 0);

    const cartError = useAppSelector((state) => state.cart.error);
    const cartMessage = useAppSelector((state) => state.cart.basketState.responseData);

    const handlePayment = async () => {
        navigate("/dashboard/cart/payment");
    };
    useEffect(() => {
        dispatch(getCart({ userId: userId }));
    }, []);

    return (
        <>
            <Bar3 headLine={"this is your cart"} />
            {cartMessage ? <SuccessAlert message={cartMessage} onClose={() => { dispatch(clearCartMessage()) }} /> : null}
            {cartError ? <ErrorAlert message={cartError} onClose={() => { dispatch(clearCartError()); dispatch(clearCartsError()) }} /> : null}
            <Typography variant="h4">Cart</Typography>
            <Typography variant="h6">Total: {totalPrice}</Typography>
            <Button onClick={handlePayment}>Pay</Button>
            {products?.map((product) => (
                <ProductInCart item={product} key={product.productId} />
            )
            )}
        </>
    )
};

export default Cart;