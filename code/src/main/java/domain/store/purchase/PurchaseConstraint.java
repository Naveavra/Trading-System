package domain.store.purchase;

import utils.Order;

public abstract class PurchaseConstraint {
    public abstract boolean handle(Order order);
}
