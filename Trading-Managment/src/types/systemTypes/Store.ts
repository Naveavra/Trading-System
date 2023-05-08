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