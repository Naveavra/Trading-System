package domain.store.discount;

import java.util.HashMap;

public abstract class DiscountConstraint {
    /**
     * This function calculates and returns the total discount(the sum needs to be substracted from the price)
     * @param basket
     * @return
     */
    public abstract int handle(HashMap<Integer,Integer> basket,HashMap<Integer,Integer> productPricing);
}
