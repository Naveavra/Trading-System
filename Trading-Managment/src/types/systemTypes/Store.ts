import { Message } from "./Message";
import { Order } from "./Order";
import { Product } from "./Product";
import { StoreRoleEnum } from "./StoreRole";

export interface Store {
    id: number;
    isActive: boolean;
    creatorId: number;
    name: string;
    description: string;
    img: string;
    rating: number;
    inventory: { id: number, product: { categories: string[], description: string, name: string, price: number, productId: number, quantity: number, img: string, rating: { value: number, content: string }[], reviewNumber: number } }[];
    storeQuestions: { questionId: number, question: Message }[];
    storeOrders: { orderId: number, totalPrice: number, productsInStores: { storeId: number, products: { productId: number, quantity: number }[][] }, userId: number }[];
    storeReviews: { messageId: number, message: Message }[];
    //todo : check obout store role
    storeRoles: { userId: number, userName: string, storeRole: StoreRoleEnum }[];
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
    storeRoles: [],
}