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

import { Product } from "./Product";

export enum status { Declined = "Declined", Approved= "Approved", Pending="Pending" };
// public String counter;
export interface Bid {
    bidId: number;
    storeId: number;
    product: Product;
    offer: Number;
    quantity: number;
    state: status;
    time: string;
    user: number;
    userName: string;
    approvers: string[];
    count: string;
}

