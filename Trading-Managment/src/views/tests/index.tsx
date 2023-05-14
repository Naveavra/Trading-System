import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../redux/store"
import { deleteStore, getStore, postStore } from "../../reducers/storesSlice";
import { Button } from "@mui/material";
import ErrorAlert from "../../components/Alerts/error";
import SuccessAlert from "../../components/Alerts/success";
import axios from "axios";


const Tests: React.FC = () => {
    const dispatch = useAppDispatch();
    const error = useAppSelector((state) => state.store.error);
    //const message = useAppSelector((state) => state.store.storeState.responseData);
    let message: unknown = null;
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
    const handleOnpostMessgae = () => {
        console.log("front post message");
        axios.post('http://localhost:4567/api/sendMessage', { userId: 0, message: "hi this is your message from menagar" })
            .then(response => {
                // Do something with the response if necessary
            })
            .catch(error => {
                // Handle the error if necessary
            });
    }

    const PING_INTERVAL = 10000; // 10 seconds in milliseconds
    // useEffect(() => {
    //     // dispatch(getShops({}));
    //     // dispatch(getProducts({ category: 'all' }));
    // }, [dispatch])
    // Send a ping to the server
    const sendPing = () => {
        axios.get('http://localhost:4567/api/wait')
            .then(response => {
                // Do something with the response if necessary
                if (response) {
                    console.log("res", response);
                    message = response.data.message;
                    console.log("message", message);
                }
            })
            .catch(error => {
                // Handle the error if necessary
            });
        // dispatch(ping(userId));
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
                onClick={handleOnpostMessgae}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'send message'}
            </Button >
            {error ? <ErrorAlert message={error} /> : null}
            {message ? <SuccessAlert message={message} /> : null}
        </>
    )
}
export default Tests;