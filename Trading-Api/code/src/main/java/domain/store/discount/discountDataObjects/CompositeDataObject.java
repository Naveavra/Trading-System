package domain.store.discount.discountDataObjects;

import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite;

import java.util.ArrayList;

public class CompositeDataObject {

    //Composite Discount types
    public NumericDiscountComposite.numeric numericType; // Max, Addition
    public LogicalDiscountComposite.logical logicalType; // Or, And, Xor
    public LogicalDiscountComposite.xorDecidingRules xorDecidingRule; // MaxDiscountValue, MinDiscountValue //used only in logical xor
    public ArrayList<DiscountDataObject> discounts;
    public ArrayList<CompositeDataObject> composites;
}
