package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.Discount;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.discount.predicates.PredicateFactory;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;

import java.util.ArrayList;

public abstract class AbstractDiscountComposite implements Discount {
//    enum logical {And,Or,Xor};
//    enum numeric {Max,Addition};
    public int discountID;
    public DiscountPredicate predicate; //used for logical Xor
    private ArrayList<Discount> discounts;
    private int storeId;
    private  GetCategoriesOperation getCategoriesOp;

    public AbstractDiscountComposite(int discountID, int storeId, GetCategoriesOperation op , LogicalDiscountComposite.logical type){
        discounts = new ArrayList<>();
        this.discountID =discountID;
        this.storeId =storeId;
        this.getCategoriesOp = op;
    }
    public AbstractDiscountComposite(int discountID, int storeId, GetCategoriesOperation op , NumericDiscountComposite.numeric type){
        discounts = new ArrayList<>();
        this.discountID =discountID;
        this.storeId =storeId;
        this.getCategoriesOp = op;
    }

    public void addDiscount(Discount dis){
        if(!discounts.contains(dis))
            discounts.add(dis);
    }
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp) {
        PredicateFactory factory = new PredicateFactory();
        DiscountPredicate pred = factory.createPredicate(type,params,storeId,getCategoriesOp);
        if(pred!=null){
            if(predicate!=null)
                predicate.setNext(pred,comp);
            else
                predicate = pred;
        }
    }


    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }
}
