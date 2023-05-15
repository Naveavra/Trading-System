package domain.store.discount;

import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import utils.orderRelated.Order;

import java.util.HashMap;

public interface Discount {
    enum DiscountTypes {Regular,Composite};
    /**
     * returns the amount needs to be subtracted
     * @param basket
     * @param order
     * @return
     */
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order);
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp);
    public DiscountPredicate getPred();
    public void setOperations(GetProductOperation getP, GetCategoriesOperation getCat);
    public void addDiscount(Discount dis);
}
