package domain.store.discount;

import domain.store.discount.compositeDiscountTypes.AbstractDiscountComposite;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.discountFunctionalInterface.GetProductOperation;
import domain.store.discount.predicates.DiscountPredicate;
import domain.user.Basket;
import org.json.JSONObject;
import utils.infoRelated.Information;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Discount extends Information {

    enum DiscountTypes {Regular,Composite};
    /**
     * returns the amount needs to be subtracted
     * @param basket
     * @param order
     * @return
     */
    public abstract double handleDiscount(Basket basket, Order order) throws Exception;
    public abstract void addPredicate(DiscountPredicate.PredicateTypes type, String params, DiscountPredicate.composore comp);
    public abstract DiscountPredicate getPred();
    public abstract void setOperations(GetProductOperation getP, GetCategoriesOperation getCat);
    public abstract void addDiscount(Discount dis);

    public abstract String getContent();
    public abstract int getDiscountID();
    public abstract void setContent(String content);
    public abstract void setDescription(String desc);
    public abstract String getDescription();
    @Override
    public JSONObject toJson(){
        JSONObject res = new JSONObject();
        res.put("id",getDiscountID());
        res.put("content",getDescription());
        return res;
    }
//    public void getDiscount(ArrayList<? extends Information> infos);

}
