package domain.store.discount.discountDataObjects;

import domain.store.discount.predicates.DiscountPredicate;
import domain.store.discount.predicates.DiscountPredicate.*;

public class PredicateDataObject {
    public PredicateDataObject(PredicateTypes predType, String params, DiscountPredicate.composore composore) {
        this.predType = predType;
        this.params = params;
        this.composore = composore;
    }

    public PredicateTypes predType; //MinPrice,MaxPrice,MinNumOfItem,MaxNumOfItem,MinNumFromCategory,MaxNumFromCategory
    public String params; //Price "<price>" ,Item "<prodId> <quantity>" ,Category "<category> <quantity>"
    public composore composore; // And, Or, Xor for more than one predicate
}
