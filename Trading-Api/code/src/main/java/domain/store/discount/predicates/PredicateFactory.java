package domain.store.discount.predicates;
import domain.store.discount.predicates.DiscountItemPredicate;
import domain.store.discount.predicates.DiscountPredicate;
import domain.store.discount.predicates.DiscountPredicate.*;
import domain.store.discount.discountFunctionalInterface.GetCategoriesOperation;
import domain.store.discount.predicates.DiscountCategoryPredicate;
import domain.store.discount.predicates.DiscountPricePredicate;

import java.util.Arrays;
import java.util.List;

public class PredicateFactory {
    public PredicateFactory(){

    }

    /**
     * function to create the different types of predicate, the params should be sent in the correct format.
     * Price Predicate : params should be a string of "<BasketPrice>"
     * Item Predicate : params should be a string of "<ProductId> <quantity>"
     * Category Predicate : params should be a string of "<Category Name> <quantity>"
     * @param type PredicateTypes Enum
     * @param params String
     * @return
     */
    public DiscountPredicate createPredicate(PredicateTypes type, String params , int storeId, GetCategoriesOperation op){
        switch (type){
            case MaxPrice,MinPrice -> {
                return new DiscountPricePredicate(Integer.parseInt(params),type,storeId);
            }
            case MaxNumOfItem, MinNumOfItem ->{
                List<String> realParams = Arrays.asList(params.split(" "));
                return new DiscountItemPredicate(Integer.parseInt(realParams.get(0)),Integer.parseInt(realParams.get(1)),type,storeId);
            }
            case MaxNumFromCategory,MinNumFromCategory -> {
                List<String> rp = Arrays.asList(params.split(" "));
                return new DiscountCategoryPredicate(rp.get(0),Integer.parseInt(rp.get(1)),type,op, storeId);
            }
        }
        return null;
    }
}
