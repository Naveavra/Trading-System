import { useEffect } from "react";
import { useAppDispatch, useAppSelector } from "../../redux/store";
import { marketStatus } from "../../reducers/adminSlice";

const MarketStatus = () => {
    const adminId = useAppSelector((state) => state.auth.userId);
    const dispatch = useAppDispatch();

    useEffect(() => {
        dispatch(marketStatus(adminId));
    }, [dispatch, adminId]);
    return (
        <div>
            <h1>Market Status</h1>
        </div>
    )
}
export default MarketStatus;