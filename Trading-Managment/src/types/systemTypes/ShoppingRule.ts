export enum ComposeRule {
    And = 'And',
    Or = 'Or',
    Conditional = 'Conditional'
}

export interface ShoppingRule {
    description: string,
    type: ShopRuleType[],
}
export type ShopRuleType = CategoryRule | ItemRule | DateTimeRule | UserRule | BasketRule;

export interface rule {
    type: 'category' | 'item' | 'dateTime' | 'user' | 'basket' | 'null';
}
export interface CategoryRule extends rule {
    category: string;
    amount: number;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface ItemRule extends rule {
    productId: number;
    amount: number;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface DateTimeRule extends rule {
    category: string;
    timeType: 'Time Limit' | 'Date Limit';
    timeLimit: string;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface UserRule extends rule {
    ageLimit: number;
    productId: number;
    category: string;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface BasketRule extends rule {
    productId: number;
    amount: number;
    ComposeRule?: ComposeRule;
};
export const emptyShoppingRuleType: ShopRuleType = {
    type: 'category',
    category: '',
    amount: 0,
    limiter: 'Min',
};
export const emptyShoppingRule: ShoppingRule = {
    description: '',
    type: [],
};