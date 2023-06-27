import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface configState {
    fetchNotify: boolean;
}
const initialState: configState = {
    fetchNotify: false,
};
const { reducer: configReducer, actions: configActions } = createSlice({
    name: 'config',
    initialState,
    reducers: {
        setFetchNotify: (state, action: PayloadAction<boolean>) => {
            state.fetchNotify = action.payload;
        },
    },
});
export const { setFetchNotify } = configActions;
export default configReducer;