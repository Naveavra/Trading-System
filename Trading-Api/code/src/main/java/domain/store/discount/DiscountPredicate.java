package domain.store.discount;

import utils.orderRelated.Order;

public interface DiscountPredicate {
    public void checkPredicate(Order order);
}
