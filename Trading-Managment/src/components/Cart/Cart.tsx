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

const Cart = () => {
    //todo: implemet redux cart functionality
    const products: CartProduct[] = [];//useAppSelector((state) => state.cart.products);
    const dispatch = useAppDispatch();
    const userId = useAppSelector((state) => state.auth.userId);

    const totalPrice = () => {
        let total = 0;
        products.forEach((item: CartProduct) => {
            total += item.quantity * item.price;
        });
        return total.toFixed(2);
    };


    const handlePayment = async () => {
        // try {
        //   const stripe = await stripePromise;
        //   const res = await makeRequest.post("/orders", {
        //     products,
        //   });
        //   await stripe.redirectToCheckout({
        //     sessionId: res.data.stripeSession.id,
        //   });

        // } catch (err) {
        //   console.log(err);
        // }
        dispatch(buyCart({ userId: userId, accountNumber: '' }))
    };
    return (
        <div className="cart">
            <h1>Products in your cart</h1>
            {products?.map((item) => (
                <div className="item" key={item.id}>
                    <img src={env.REACT_APP_UPLOAD_URL + item.img} alt="" />
                    <div className="details">
                        <h1>{item.name}</h1>
                        <p>{item.description.substring(0, 100)}</p>
                        <div className="price">
                            {item.quantity} x ${item.price}
                        </div>
                    </div>
                    <DeleteOutlinedIcon
                        className="delete"
                        onClick={() => dispatch(removeFromCart({ userId: userId, storeId: item.storeId, productId: item.id, quantity: item.quantity }))}
                    />
                </div>
            ))}
            <div className="total">
                <span>SUBTOTAL</span>
                <span>${totalPrice()}</span>
            </div>
            <button onClick={handlePayment}>PROCEED TO CHECKOUT</button>
            <span className="reset" onClick={() => dispatch(resetCart())}>
                Reset Cart
            </span>
        </div>
    );
};

export default Cart;