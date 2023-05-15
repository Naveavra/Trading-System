package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.Discount;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import utils.orderRelated.Order;

import java.util.HashMap;

public class NumericDiscountComposite extends AbstractDiscountComposite{


    public enum numeric {Max,Addition};
    private numeric type;
    public NumericDiscountComposite(int discountID, int storeId, GetCategoriesOperation op, numeric type, GetProductOperation getProductOp) {
        super(discountID, storeId, op, type,getProductOp);
        this.type = type;
    }


    @Override
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order) throws Exception {
        double discountValue = 0;
        switch (type){
            case Max -> {
                for(Discount dis : getDiscounts()){
                    double temp = dis.handleDiscount(basket,order);
                    if(discountValue<temp)
                        discountValue = temp;
                }
            }
            case Addition -> {
                for(Discount dis : getDiscounts()){
                    discountValue += dis.handleDiscount(basket,order);
                }
            }
        }
        return discountValue;
    }

    @Override
    public DiscountPredicate getPred() {
        return predicate;
    }
    @Override
    public void setDecidingRule(LogicalDiscountComposite.xorDecidingRules rule) {

    }

    @Override
    public LogicalDiscountComposite.xorDecidingRules getDecidingRule() {
        return null;
    }

}
