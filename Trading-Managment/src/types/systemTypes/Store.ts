import { Appointment } from "./Appointment";
import { Bid } from "./Bid";
import { Message, Question, Review } from "./Message";
import { Order } from "./Order";
import { Product } from "./Product";
import { RoleInStore, StoreRoleEnum } from "./StoreRole";

export interface Store {
    storeId: number;
    isActive: boolean;
    creatorId: number;
    storeName: string;
    description: string;
    img: string;
    rating: number;
    inventory: Product[];
    questions: Question[];
    storeOrders: Order[];
    reviews: Review[];
    appHistory: Appointment[];
    roles: RoleInStore[];
    bids: Bid[];
    discounts: string[];
    purchasePolicies: string[];
    //todo add purchase and discount policy
}
export const emptyStore: Store = {
    storeId: -1,
    isActive: false,
    creatorId: -1,
    storeName: "",
    description: "",
    img: "",
    rating: -1,
    inventory: [],
    questions: [],
    storeOrders: [],
    reviews: [],
    roles: [],
    appHistory: [],
    bids: [],
    discounts: [],
    shopingRules: []
}
//    inventory: { id: number, product: { categories: string[], description: string, name: string, price: number, productId: number, quantity: number, img: string, rating: { value: number, content: string }[], reviewNumber: number } }[];
//    storeOrders: { orderId: number, totalPrice: number, productsInStores: { storeId: number, products: { productId: number, quantity: number }[][] }, userId: number }[];
