package domain.store.discount;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DiscountPolicy {

    ConcurrentLinkedDeque<DiscountConstraint> discounts;
    public DiscountPolicy(){
        discounts = new ConcurrentLinkedDeque<>();
    }

    /**
     * calculates the entire discount for the products.
     * @param basket
     * @param productPricing
     * @return
     */
    public int handleDiscounts(HashMap<Integer,Integer> basket, HashMap<Integer,Integer> productPricing){
        int totalDiscount = 0;
        for(DiscountConstraint discount:discounts){
            totalDiscount=discount.handle(basket,productPricing);
        }
        return totalDiscount;
    }

    public boolean createConstraint(String policy) {
        //TODO handle parsing the constraint (probably using Stanford NLP library)
        discounts.add(new ConcreteDiscountConstraint());
        return true;
    }
}
