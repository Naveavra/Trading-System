package domain.store.discount.compositeDiscountTypes;

import domain.store.discount.AbstractDiscount;
import domain.store.discount.Discount;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.discount.predicates.PredicateFactory;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import org.json.JSONObject;
import utils.infoRelated.Information;

import java.util.ArrayList;

public abstract class AbstractDiscountComposite  extends Discount {
//    enum logical {And,Or,Xor};
//    enum numeric {Max,Addition};
    public int discountID;
    public DiscountPredicate predicate;
    private ArrayList<Discount> discounts;
    private String description;
    private String content;
    private int storeId;
    private  GetCategoriesOperation getCategoriesOp;
    private GetProductOperation getProductOp;

    public AbstractDiscountComposite(int discountID, int storeId, GetCategoriesOperation op, LogicalDiscountComposite.logical type, GetProductOperation getProductOp){
        discounts = new ArrayList<>();
        this.discountID =discountID;
        this.storeId =storeId;
        this.getCategoriesOp = op;
        this.getProductOp = getProductOp;
    }
    public AbstractDiscountComposite(int discountID, int storeId, GetCategoriesOperation op, NumericDiscountComposite.numeric type, GetProductOperation getProductOp){
        discounts = new ArrayList<>();
        this.discountID =discountID;
        this.storeId =storeId;
        this.getCategoriesOp = op;
        this.getProductOp = getProductOp;
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


    public void setOperations(GetProductOperation getProductOp, GetCategoriesOperation getCategoryOp){
        this.getProductOp = getProductOp;
        this.getCategoriesOp = getCategoryOp;
    }
    public ArrayList<Discount> getDiscounts() {
        return discounts;
    }
    public abstract void setDecidingRule(LogicalDiscountComposite.xorDecidingRules rule);
    public abstract LogicalDiscountComposite.xorDecidingRules  getDecidingRule();

    @Override
    public void setContent(String content) {
        this.content=content;
    }
    @Override
    public String getContent(){return this.content;}
    @Override
    public int getDiscountID() {
        return this.discountID;
    }
    @Override
    public String getDescription(){
        return this.description;
    }
    @Override
    public void setDescription(String desc){
        this.description=desc;
    }


}
