package domain.store.discount;

import utils.orderRelated.Order;

public interface Discount {
    public void handleDiscount(Order order);
}
