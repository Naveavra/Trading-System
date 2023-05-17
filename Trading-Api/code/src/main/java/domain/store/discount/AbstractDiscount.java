package domain.store.discount;

import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.discount.predicates.PredicateFactory;

public abstract class AbstractDiscount implements Discount{
    public enum discountTypes{Product,Category,Store}

    //same for all discounts
    public int discountID;
    private int storeId;
    private double percentage;
    public DiscountPredicate predicate = null; //if not null it's a conditional discount.


    private String discountedCategory; //used in discountOnCategory
    private int discountedProductID = -1; //used by discount on item

    //functional interfaces
    public GetProductOperation getProductOp;
    public GetCategoriesOperation getCategoriesOp;
    public AbstractDiscount(){}
    public AbstractDiscount(int discountID,int storeId,double percentage,String discountedCategory){
        this.discountID = discountID;
        this.storeId = storeId;
        this.percentage = percentage;
        this.discountedCategory = discountedCategory;
    }

    // Setters
    @Override
    public void setOperations(GetProductOperation getP,GetCategoriesOperation getCat){
        this.getProductOp = getP;
        this.getCategoriesOp = getCat;
    }

    @Override
    public void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp){
        PredicateFactory factory = new PredicateFactory();
        DiscountPredicate pred = factory.createPredicate(type,params,storeId,getCategoriesOp);
        if(pred!=null){
            if(predicate!=null)
                predicate.setNext(pred,comp);
            else
                predicate = pred;
        }
    }
    @Override
    public void addDiscount(Discount dis){}

    // Getters
    public DiscountPredicate getPred(){
        return predicate;
    }
    public int getStoreId() {
        return storeId;
    }
    public double getPercentage() {
        return 100 - percentage;
    }

    public void setDiscountedProductID(int discountedProductID) {
        this.discountedProductID = discountedProductID;
    }

    public int getDiscountedProductID() {
        return discountedProductID;
    }

    public String getDiscountedCategory() {
        return discountedCategory;
    }

//    public void setPercentage(double percentage) {
//        this.percentage = percentage;
//    }
}
