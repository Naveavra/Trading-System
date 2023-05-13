import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../redux/store"
import { appointManager, appointOwner, deleteStore, fireManager, fireOwner, getStore, postStore } from "../../reducers/storesSlice";
import { Button } from "@mui/material";
import ErrorAlert from "../../components/Alerts/error";
import SuccessAlert from "../../components/Alerts/success";
import { getProducts, patchProduct, postProduct } from "../../reducers/productsSlice";

const Tests: React.FC = () => {
    const dispatch = useAppDispatch();
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
    const handleOnPatchProducts = () => {
        console.log("front patch products")
        dispatch(patchProduct({id: 1, storeId: 1, productId: 0, category: [], name: "mazda 6", description: null, price: 50, quantity: 10, img: ""}))
    }
    const handleOnAppointManager = () => {
        console.log("front appoint products")
        dispatch(appointManager({storeId: 0,userIncharge: 1, newOwner: 2}))
    }
    const handleOnAppointOwner = () => {
        console.log("front appoint products")
        dispatch(appointOwner({storeId: 0,userIncharge: 1, newOwner: 2}))
    }
    const handleOnFireManager = () => {
        console.log("front appoint products")
        dispatch(fireManager({storeId: 0,userIncharge: 1, newOwner: 2}))
    }
    const handleOnFireOwner = () => {
        console.log("front appoint products")
        dispatch(fireOwner({storeId: 0,userIncharge: 1, newOwner: 2}))
    }
    useEffect(() => {
        //dispatch(getStores());
    }, [dispatch])
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
            {error ? <ErrorAlert message={error} /> : null}
            {message ? <SuccessAlert message={message} /> : null}
        </>
    )
}
export default Tests;