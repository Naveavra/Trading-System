// public enum status {Declined,Approved,Pending};
// public int bidId;
// public double offer; //how much user has offered for Product.
// public int quantity; //how much does he want to buy.
// public Product product;
// public status approved;
// public LocalDateTime bidTime;
// public Member user;
// public ArrayList<String> approveCount;
// public ArrayList<String> approvers;

import { EmptyProduct, Product } from "./Product";

export enum status { Declined = "Declined", Approved = "Approved", Pending = "Pending",Counter= "Counter", Completed = "Completed"};
// public String counter;
export interface Bid {
    bidId: number;
    storeId: number;
    offer: Number;
    product: Product;
    quantity: number;
    userId: number;
    state: status;
    time: string;
    approved: string[];
    notApproved: string[];
    count: string;
}
export const EmptyBid: Bid = {
    bidId: -1,
    storeId: -1,
    product: EmptyProduct,
    offer: -1,
    quantity: -1,
    state: status.Pending,
    time: '',
    userId: -1,
    approved: [],
    notApproved: [],
    count: '',
}

