import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../redux/store"
import { deleteStore, getStore, postStore } from "../../reducers/storesSlice";
import { Button } from "@mui/material";
import ErrorAlert from "../../components/Alerts/error";
import SuccessAlert from "../../components/Alerts/success";

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
            {error ? <ErrorAlert message={error} /> : null}
            {message ? <SuccessAlert message={message} /> : null}
        </>
    )
}
export default Tests;