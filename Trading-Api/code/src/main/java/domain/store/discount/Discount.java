package domain.store.discount;

import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import domain.user.Basket;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.HashMap;

public interface Discount {
    void setContent(String content);

    enum DiscountTypes {Regular,Composite};
    /**
     * returns the amount needs to be subtracted
     * @param basket
     * @param order
     * @return
     */
    public double handleDiscount(Basket basket, Order order) throws Exception;
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp);
    public DiscountPredicate getPred();
    public void setOperations(GetProductOperation getP, GetCategoriesOperation getCat);
    public void addDiscount(Discount dis);

    public String getContent();
    public int getDiscountID();
}
