package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.DiscountPredicate;
import utils.orderRelated.Order;

import java.util.HashMap;

public class NumericDiscountComposite extends AbstractDiscountComposite{
    @Override
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order) {
        //TODO
    }

    @Override
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp) {
        
    }
}
