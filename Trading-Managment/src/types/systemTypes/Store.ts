import { Message } from "./Message";
import { Order } from "./Order";
import { Product } from "./Product";

export interface Store {
    id: number;
    name: string;
    description: string;
    image: string;
    rating: number;
    isActive: boolean;
    inventory: Product[];
    storeQuestions: {questionId: number, question: Message}[];
    storeOrders: {orderId: number, order: Order}[];
    storeReviews: {messageId: number, message: Message}[];
}
export const emptyStore:Store ={
    id: -1,
    name: "",
    description: "",
    image: "",
    rating: -1,
    isActive: false,
    inventory: [],
    storeQuestions: [],
    storeOrders:[],
    storeReviews:[],
}