package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.Discount;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import utils.orderRelated.Order;

import java.util.HashMap;

public class LogicalDiscountComposite extends AbstractDiscountComposite{
    private logical type;
    public LogicalDiscountComposite(int discountID, int storeId, GetCategoriesOperation op, logical type) {
        super(discountID, storeId, op, type);
        this.type = type;
    }

    enum logical {And,Or,Xor};

    @Override
    public double handleDiscount(HashMap<Integer, Integer> basket, Order order) {
        double discountValue = 0;
        switch (type){
            case Or -> { //TODO FIX MAYBE
                for(Discount dis: getDiscounts()){
                    discountValue += dis.handleDiscount(basket,order);
                }
            }
            case And -> {
                for(Discount dis : getDiscounts()){
                    if(!dis.getPred().checkPredicate(order)){
                        return 0;
                    }
                    discountValue += dis.handleDiscount(basket,order);
                }
            }
            case Xor -> {}//TODO FIND OUT WHAT TO DO WITH THIS;
        }
        return discountValue;
    }

    @Override
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp) {
        this.predicate = predicate;
    }

    @Override
    public DiscountPredicate getPred() {
        return predicate;
    }
}
