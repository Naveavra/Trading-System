package domain.store.discount;

import domain.store.order.Order;

import java.util.HashMap;

public class ConcreteDiscountConstraint extends DiscountConstraint{
    @Override
    public int handle(HashMap<Integer,Integer> basket,HashMap<Integer,Integer> productPricing){
        return 0;
    }
}
