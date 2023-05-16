import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../redux/store"
import { appointManager, appointOwner, deleteStore, fireManager, fireOwner, getStore, postStore } from "../../reducers/storesSlice";
import { Button } from "@mui/material";
import ErrorAlert from "../../components/Alerts/error";
import SuccessAlert from "../../components/Alerts/success";
import { getProducts, patchProduct, postProduct } from "../../reducers/productsSlice";
import { deleteCart, getCart, patchCart, postBasket } from "../../reducers/cartSlice";
import { Basket } from "../../types/systemTypes/Basket";
import { PatchCartParams } from "../../types/requestTypes/cartTypes";
import axios from "axios";
const Tests: React.FC = () => {
    const dispatch = useAppDispatch();
    const id = 1;
    const error = useAppSelector((state) => state.store.error);
    const message = useAppSelector((state) => state.store.storeState.responseData);

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
        dispatch(deleteStore({ userId: 1, storeId: 0 }));
    }

    const handleOnAddProduct = () => {
        console.log("front add product")
        dispatch(postProduct({ id: 1, storeId: 1, category: [], name: "mazda 3", description: "ziv's mazda", price: 5, quantity: 5, img: "" }))
    }
    const handleOnPatchProduct = () => {
        // console.log("frontpatch product")
        // dispatch(patchProduct({
        //     id: 1, storeId: 1, productId: 1, category: [""], name: "", cription: "",
        //     price: number | null;
        //     quantity: number | null;
        //     img: string | null;
        // }))
    }
    //const retryConnection = useRef(0);

    // const newConnection = hubConnection('http://localhost:4567/', { logging: true, });
    // const hubProxy = newConnection.createHubProxy('NotificationHub');
    // hubProxy.on('firstConnection', () => { });
    // newConnection.start()
    //     .done(() => {
    //         retryConnection.current = 0;
    //     })
    //     .fail((e: any) => {
    //         console.log(e);
    //         console.log("signalR could not connect")
    //     })
    // // set up event listeners i.e. for incoming "message" event
    // hubProxy.on('SendNotification', function (idToSend, message) {

    //     if (idToSend === id) {
    //         console.log("messga",message);
    //         //setNotification(message);
    //     }
    // });
    // newConnection.disconnected(() => {
    //     if (retryConnection.current <= 10) {
    //         retryConnection.current++;
    //         setTimeout(() => {
    //             newConnection.start().done(() => {
    //                 retryConnection.current = 0;
    //             });
    //         }, 10000)
    //     }
    // })

    const [message_notification, setMessage] = useState("");

    useEffect(() => {
        const waitForMessage = async () => {
            try {
                const response = await axios.get("http://localhost:4567/wait");
                if (response.status === 200) {
                    setMessage(response.data);
                }
            } catch (error) {
                console.error(error);
            } finally {
                waitForMessage();
            }
        };
        waitForMessage();
    }, []);

    const sendMessage = async () => {
        try {
            const message = "Hello, world!";
            await axios.post("http://localhost:4567/api/sendMessage", { message: message, id: id });
        } catch (error) {
            console.error(error);
        }
    };

    const handleOnGetProducts = () => {
        console.log("front get products");
        dispatch(getProducts({ storeId: 1 }));
    }
    const handleOnPatchProducts = () => {
        console.log("front patch products");
        dispatch(patchProduct({ id: 1, storeId: 1, productId: 0, category: [], name: "mazda 6", description: null, price: 50, quantity: 10, img: "" }));
    }
    const handleOnAppointManager = () => {
        console.log("front appoint products");
        dispatch(appointManager({ storeId: 0, userIncharge: 1, newOwner: 2 }));
    }
    const handleOnAppointOwner = () => {
        console.log("front appoint products");
        dispatch(appointOwner({ storeId: 0, userIncharge: 1, newOwner: 2 }));
    }
    const handleOnFireManager = () => {
        console.log("front appoint products");
        dispatch(fireManager({ storeId: 0, userIncharge: 1, newOwner: 2 }));
    }
    const handleOnFireOwner = () => {
        console.log("front appoint products");
        dispatch(fireOwner({ storeId: 0, userIncharge: 1, newOwner: 2 }));
    }

    const handleOnPostBasket = () => {
        const data: { productId: number, quantity: number }[] = [
            { productId: 1, quantity: 5 },
            { productId: 2, quantity: 3 },
            { productId: 3, quantity: 2 }
        ];
        const basket: Basket = {
            productsList: data
        };
        console.log("front appoint products");
        dispatch(postBasket({ userId: 0, storeId: 5, basket: basket }));
    }

    const handleOnPatchCart = () => {
        dispatch(patchCart({ userId: 0, storeId: 0, productId: 1, quantity: 5 })).then((response => {
            const responseData = response.payload;
            console.log(responseData);
        }))
    }
    const handleOnGetCart = () => {
        dispatch(getCart({ userId: 0 })).then((response) => {
            const responseData = response.payload; // Access the response data
            console.log(responseData);
            // Perform further operations with the response data
        });
        //dispatch(getCart({userId: 0}));
    }

    const handleOnDeleteCart = () => {
        dispatch(deleteCart({ userId: 0 }));
    }


    // useEffect(() => {
    //     sendPing();
    //     // Call the sendPing function every 2 seconds
    //     // console.log("message");
    //     if (message) {
    //         message = null;
    //         const pingInterval = setInterval(sendPing, PING_INTERVAL);
    //     }

    //     // // Stop the ping interval when the user leaves the app
    //     // return () => {
    //     //     console.log("un messgae");
    //     //     clearInterval(pingInterval)
    //     // };
    // }, [message, dispatch])
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
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnGetProducts}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'get product'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnPatchProducts}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'patch product'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnAppointManager}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'appoint manager'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnAppointOwner}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'appoint owner'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnFireManager}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'fire manager'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnFireOwner}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'fire owner'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnPostBasket}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'post basket'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnPatchCart}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'patch cart'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnGetCart}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'get cart'}
            </Button >
            <Button
                type="submit"
                fullWidth
                variant="contained"
                onClick={handleOnDeleteCart}
                sx={{ color: 'black', '&:hover': { backgroundColor: 'green' }, width: '50%', }}
            >
                {'delete cart'}
            </Button >
            {error ? <ErrorAlert message={error} /> : null}
            {message ? <SuccessAlert message={message} /> : null}
        </>
    )
}
export default Tests;