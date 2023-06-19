package domain.store.discount.discountDataObjects;

import domain.store.discount.Discount;
import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite;

import java.util.ArrayList;

public class CompositeDataObject {
    public CompositeDataObject(double percentage, NumericDiscountComposite.numeric numericType, LogicalDiscountComposite.logical logicalType,
                               LogicalDiscountComposite.xorDecidingRules xorDecidingRule, ArrayList<DiscountDataObject> discounts, ArrayList<CompositeDataObject> composites) {
        this.percentage = percentage;
        this.numericType = numericType;
        this.logicalType = logicalType;
        this.xorDecidingRule = xorDecidingRule;
        this.discounts = discounts;
        this.composites = composites;
        if(discounts == null){
            this.discounts = new ArrayList<>();
        }
        if(composites == null){
            this.composites = new ArrayList<>();
        }
    }

    public double percentage;
    //Composite Discount types
    public NumericDiscountComposite.numeric numericType; // Max, Addition
    public LogicalDiscountComposite.logical logicalType; // Or, And, Xor
    public LogicalDiscountComposite.xorDecidingRules xorDecidingRule; // MaxDiscountValue, MinDiscountValue //used only in logical xor
    public ArrayList<DiscountDataObject> discounts;
    public ArrayList<CompositeDataObject> composites;

    public void addToDiscounts(DiscountDataObject obj){
        if(!discounts.contains(obj))
            this.discounts.add(obj);
    }

    public void addToComposites(CompositeDataObject obj){
        if(!composites.contains(obj))
            this.composites.add(obj);
    }

    public void setDiscounts(ArrayList<DiscountDataObject> dataObjects){
        this.discounts = dataObjects;
    }

    public void setComposites(ArrayList<CompositeDataObject> dataObjects){
        this.composites = dataObjects;
    }

}
