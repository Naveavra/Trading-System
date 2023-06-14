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

export enum status { Declined, Approved, Pending };
// public String counter;
export interface Bid {
    id: number;
    storeId: number;
    product: Product;
    offer: Number;
    quantity: number;
    approved: status;
    bidTime: string;
    userId: number;
    userName: string;
    approvers: string[];
}

