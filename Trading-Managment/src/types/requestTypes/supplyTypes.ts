export interface SupplyParams {
    name: string;
    address: string;
    city: string;
    country: string;
    zip: string;
}

export interface CancelSupplyParams {
    transaction_id: number;
} 