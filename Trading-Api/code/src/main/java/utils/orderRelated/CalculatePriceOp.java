package utils.orderRelated;

import java.util.HashMap;

/**
 * Functional interface used to calculate price of specific items in a store (without discounts)
 */
public interface CalculatePriceOp {
    public int calculatePrice(HashMap<Integer, HashMap<Integer, Integer>> basket) throws Exception;
}
