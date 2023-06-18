import { ApiResponse } from "../types/apiTypes";
import { addBidParams, answerBidParams, buyProductInBidParams, counterBidParams, editBidParams } from "../types/requestTypes/bidTypes";
import { getApiClient } from "./apiClient";
import { apiErrorHandlerWrapper } from "./util";

export const bidApi = {

    postBid: (params: addBidParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/biddings/addBid', params)),

    postAnswerBid: (params: answerBidParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/biddings/answerBid', params)),

    postCounterBid: (params: counterBidParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/biddings/counterBid', params)),

    editBid: (params: editBidParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/biddings/editBid', params)),

    buyProductInBid: (params: buyProductInBidParams): Promise<ApiResponse<string>> =>
        apiErrorHandlerWrapper(getApiClient().post('api/biddings/bidPayment', params)),
}