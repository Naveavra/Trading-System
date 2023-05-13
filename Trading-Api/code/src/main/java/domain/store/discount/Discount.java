package domain.store.discount;

import utils.orderRelated.Order;

import java.util.HashMap;

public interface Discount {
    /**
     * returns the amount needs to be subtracted
     * @param basket
     * @param order
     * @return
     */
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order);
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params,DiscountPredicate.composore comp);
}
