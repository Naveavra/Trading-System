package domain.store.discount;

import utils.orderRelated.Order;

public abstract class SimpleDiscount implements Discount{
    public DiscountPredicate predicate; //if not null it's a conditional discount.
    public int percentage;
    @Override
    public void handleDiscount(Order order) {
        //TODO;
    }
}
