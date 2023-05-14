package domain.store.discount.discountDataObjects;

import domain.store.discount.AbstractDiscount.*;
import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite.*;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite.*;
import domain.store.discount.predicates.DiscountPredicate;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscountDataObject {
    //regular
    public discountTypes discountType; //choose discount type
    public int prodId; //used for productDiscount, this is the id of the discounted product.
    public String discountedCategory ="";
    //conditional
    public ArrayList<PredicateDataObject> predicates;
}
