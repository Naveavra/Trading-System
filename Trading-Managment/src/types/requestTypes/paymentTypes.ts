export interface HandShakeParams {
    action_type: string;
};
export interface PayParams {
    card_number: string;
    month: string;
    year: string;
    holder: string;
    ccv: string;
    id: number;
}
export interface CancelPayParams {
    transaction_id: number;
}