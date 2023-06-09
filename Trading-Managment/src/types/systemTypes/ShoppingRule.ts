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

export interface CategoryRule {
    type: 'category';
    category: string;
    amount: number;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface ItemRule {
    type: 'item';
    productId: number;
    amount: number;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface DateTimeRule {
    type: 'dateTime';
    timeType: 'Time Limit' | 'Date Limit';
    timeLimit: string;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface UserRule {
    type: 'user';
    ageLimit: number;
    productId: number;
    limiter: 'Min' | 'Max';
    ComposeRule?: ComposeRule;
};
export interface BasketRule {
    type: 'basket';
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