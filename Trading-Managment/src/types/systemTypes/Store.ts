import { Message } from "./Message";
import { Order } from "./Order";
import { Product } from "./Product";

export interface Store {
    id: number;
    isActive: boolean;
    creatorId: number;
    name: string;
    description: string;
    img: string;
    rating: number;
    inventory: Product[];
    storeQuestions: { questionId: number, question: Message }[];
    storeOrders: { orderId: number, order: Order }[];
    storeReviews: { messageId: number, message: Message }[];
    appHistory: {fatherId: string, childId: string[]} [];
    //todo add purchase and discount policy
}
export const emptyStore: Store = {
    id: -1,
    isActive: false,
    creatorId: -1,
    name: "",
    description: "",
    img: "",
    rating: -1,
    inventory: [],
    storeQuestions: [],
    storeOrders: [],
    storeReviews: [],
    appHistory:[]
}