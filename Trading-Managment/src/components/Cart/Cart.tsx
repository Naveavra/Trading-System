import "./Cart.css";
//import { removeItem, resetCart } from "../../redux/cartReducer";
;
//import { makeRequest } from "../../makeRequest";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { Button, Typography } from "@mui/material";
import ProductInCart from "../ProductInCart/ProductCart";
import { useNavigate } from "react-router-dom";
import Bar3 from "../Bars/Navbar/NavBar3";

const Cart = () => {
    const navigate = useNavigate();
    const dispatch = useAppDispatch();

    const cart = useAppSelector((state) => state.cart.responseData);
    const products = cart?.flat();
    const totalPrice = products?.reduce((acc, product) => acc + product.quantity * product.price, 0);


    const handlePayment = async () => {
        navigate("/dashboard/cart/payment");
    };

    return (
        <>
            <Bar3 headLine={"this is your cart"} />
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