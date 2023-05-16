import { Basket } from "./Basket";

export interface Cart {
    baskets: { storeId: number, products: Basket }[]
}
export const emptyCart: Cart = {
    baskets: []
}