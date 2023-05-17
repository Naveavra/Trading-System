package domain.store.purchase;

import utils.orderRelated.Order;

public abstract class PurchaseConstraint {
    public abstract boolean handle(Order order);
}
