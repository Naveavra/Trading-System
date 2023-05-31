package utils.orderRelated;

import domain.user.ShoppingCart;

import java.util.HashMap;

/**
 * Functional interface used to calculate price of specific items in a store (without discounts)
 */
public interface CalculatePriceOp {
    public int calculatePrice(ShoppingCart basket) throws Exception;
}
