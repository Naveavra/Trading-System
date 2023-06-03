package domain.store.discount;

import domain.store.discount.compositeDiscountTypes.LogicalDiscountComposite;
import domain.store.discount.compositeDiscountTypes.NumericDiscountComposite;
import domain.store.discount.discountDataObjects.CompositeDataObject;
import domain.store.discount.discountDataObjects.DiscountDataObject;
import domain.store.discount.discountDataObjects.PredicateDataObject;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.user.Basket;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscountFactory {
    public int storeID;
    private AtomicInteger discountIds;
    private GetProductOperation getProdOp;
    private GetCategoriesOperation getCategoryOp;
    public DiscountFactory(int storeID, GetProductOperation getProdOp, GetCategoriesOperation getCategoryOp){
        this.storeID = storeID;
        discountIds = new AtomicInteger();
        this.getProdOp = getProdOp;
        this.getCategoryOp = getCategoryOp;
    }
    public synchronized Discount createDiscount(CompositeDataObject discountData) throws Exception {
        Discount concreteDiscount = null;
        if(discountData.numericType != null){
            concreteDiscount = new NumericDiscountComposite(discountIds.getAndIncrement(),storeID,getCategoryOp,discountData.numericType,getProdOp);
        }
        else {
            switch (discountData.logicalType){
                case Xor -> {
                    LogicalDiscountComposite temp = new LogicalDiscountComposite(discountIds.getAndIncrement(),storeID,getCategoryOp,discountData.logicalType,getProdOp);
                    temp.setDecidingRule(discountData.xorDecidingRule);
                    concreteDiscount = temp;
                }
                default -> {
                    concreteDiscount = new LogicalDiscountComposite(discountIds.getAndIncrement(),storeID,getCategoryOp,discountData.logicalType,getProdOp);
                }
            }
        }
        if(!validSize(discountData.discounts,discountData.composites)){
            throw new Exception("Not enough information about the discounts you're trying to compose");
        }
        for(DiscountDataObject dis : discountData.discounts){
            concreteDiscount.addDiscount(createDiscount(dis));
        }
        for(CompositeDataObject comp : discountData.composites){
            concreteDiscount.addDiscount(createDiscount(comp));
        }
        return concreteDiscount;
    }

    public boolean validSize(ArrayList<DiscountDataObject> discounts, ArrayList<CompositeDataObject> composites) {
        if(discounts!=null && composites!=null && discounts.size()+composites.size() >= 2)
            return true;
        if(discounts!=null && discounts.size() >= 2)
            return true;
        if(composites!=null && composites.size() >= 2)
            return true;
        return false;
    }
    public synchronized Discount createDiscount(DiscountDataObject discountData){
        Discount concreteDiscount = new AbstractDiscount() {


            @Override
            public double handleDiscount(Basket basket, Order order) {
                return 0;
            }
        };
        switch (discountData.discountType){
            case Store -> {
                concreteDiscount = new DiscountOnStore(discountIds.getAndIncrement(),storeID,discountData.percentage,"");
            }
            case Product -> {
                DiscountOnItem temp = new DiscountOnItem(discountIds.getAndIncrement(),storeID,discountData.percentage,"");
                temp.setDiscountedProductID(discountData.prodId);
                concreteDiscount = temp;
            }
            case Category -> {
                DiscountOnCategory temp = new DiscountOnCategory(discountIds.getAndIncrement(),storeID,discountData.percentage, discountData.discountedCategory);
                concreteDiscount = temp;
            }
        }
        concreteDiscount.setOperations(getProdOp,getCategoryOp);

        return discountData.predicates.isEmpty()? concreteDiscount : addPredicates(concreteDiscount,discountData);
    }

    private Discount addPredicates(Discount concreteDiscount, DiscountDataObject discountData) {
        ArrayList<PredicateDataObject> predicates = discountData.predicates;
        if(!predicates.isEmpty()){
            for(PredicateDataObject pred : predicates){
                concreteDiscount.addPredicate(pred.predType, pred.params, pred.composore);
            }
        }
        return concreteDiscount;
    }
}
