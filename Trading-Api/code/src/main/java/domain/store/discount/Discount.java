package domain.store.discount;

import utils.orderRelated.Order;

import java.util.HashMap;

public interface Discount {
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order);
    public void addPredicate(DiscountPredicate predicate);
}
