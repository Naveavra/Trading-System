import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../redux/store"
import { deleteStore, getStore, postStore } from "../../reducers/storesSlice";
import { Button } from "@mui/material";
import ErrorAlert from "../../components/Alerts/error";
import SuccessAlert from "../../components/Alerts/success";
import { getProducts, postProduct } from "../../reducers/productsSlice";

const Tests: React.FC = () => {
    const dispatch = useAppDispatch();
    const error = useAppSelector((state) => state.store.error);
    const message = useAppSelector((state) => state.store.storeState.responseData);
    const producterror = useAppSelector((state) => state.product.productState.error); //this is for single product
    const productmessage = useAppSelector((state) => state.product.productState.responseData);
    //const stores_response = useAppSelector((state) => state.store.responseData);
    //const stores: Store[] = stores_response.data.results ?? [];
    // console.log("stores",stores);
    const handleOnAddStore = () => {
        console.log("front add store");
        dispatch(postStore({ userId: 1, desc: "new store" }));
        // dispatch(getStores());
    }
    const handleOnRemove = () => {
        console.log("front remove store");
        dispatch(deleteStore({ userId: 1, storeId: 0 }))
    }

    const handleOnAddProduct = () => {
        console.log("front add product")
        dispatch(postProduct({id: 1, storeId: 1, category: [], name: "mazda 3", description: "ziv's mazda", price: 5, quantity: 5, img: ""}))
    }
    const handleOnGetProducts = () => {
        console.log("front get products")
        dispatch(getProducts({storeId: 1}))
    }
    useEffect(() => {
        sendPing();
        // Call the sendPing function every 2 seconds
        // console.log("message");
        if (message) {
            message = null;
            const pingInterval = setInterval(sendPing, PING_INTERVAL);
        }

        // // Stop the ping interval when the user leaves the app
        // return () => {
        //     console.log("un messgae");
        //     clearInterval(pingInterval)
        // };
    }, [message, dispatch])
    return (
        <>
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnAddStore}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'add store'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnRemove}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'remove store'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnAddProduct}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'add product'}
            </Button ><Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnGetProducts}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'get product'}
            </Button >
            {error ? <ErrorAlert message={error} /> : null}
            {message ? <SuccessAlert message={message} /> : null}
        </>
    )
}
export default Tests;