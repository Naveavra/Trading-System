package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.Discount;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.user.Basket;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.HashMap;

public class LogicalDiscountComposite extends AbstractDiscountComposite{
    private logical type;
    private xorDecidingRules decidingRule;
    public LogicalDiscountComposite(int discountID, int storeId, GetCategoriesOperation op, logical type, GetProductOperation getProductOp) {
        super(discountID, storeId, op, type,getProductOp );
        this.type = type;
    }

    public enum logical {And,Or,Xor};
    public enum xorDecidingRules {MaxDiscountValue,MinDiscountValue}
    @Override
    public double handleDiscount(Basket basket, Order order) throws Exception {
        double discountValue = 0;
        switch (type){
            case Or -> {
                for(Discount dis: getDiscounts()){
                    //TODO MAYBE SPLIT THE PERCENTAGE SO IT WONT DOUBLE IF ALL HANDLES SUCCEED;
                    discountValue += dis.handleDiscount(basket,order);
                }
            }
            case And -> {
                for(Discount dis : getDiscounts()){
                    if(dis.getPred()!=null && !dis.getPred().checkPredicate(order)){
                        return 0;
                    }
                    discountValue += dis.handleDiscount(basket,order);
                }
            }
            case Xor -> {
                switch (decidingRule){
                    case MaxDiscountValue -> discountValue = handleMaxDiscountValue(basket,order);
                    case MinDiscountValue -> discountValue = handleMinDiscountValue(basket,order);

                }

            }
        }
        return discountValue;
    }


    private double handleMinDiscountValue(Basket basket, Order order) throws Exception {
        double ans =Integer.MAX_VALUE;
        for(Discount dis: getDiscounts()){
            double disVal = dis.handleDiscount(basket,order);
            if(ans > disVal){
                ans = disVal;
            }
        }
        return ans<Integer.MAX_VALUE ? ans : 0;
    }

    private double handleMaxDiscountValue(Basket basket,Order order) throws Exception {
        double ans =0;
        for(Discount dis: getDiscounts()){
            double disVal = dis.handleDiscount(basket,order);
            if(ans < disVal){
                ans = disVal;
            }
        }
        return ans;
    }


    @Override
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp) {
        this.predicate = predicate;
    }

    @Override
    public DiscountPredicate getPred() {
        return predicate;
    }

    @Override
    public void setDecidingRule(xorDecidingRules decidingRule) {
        this.decidingRule = decidingRule;
    }

    @Override
    public xorDecidingRules getDecidingRule() {
        return decidingRule;
    }

}
