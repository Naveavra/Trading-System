package domain.store.discount.discountDataObjects;

import domain.store.discount.predicates.DiscountPredicate.*;

public class PredicateDataObject {
    public PredicateTypes predType; //MinPrice,MaxPrice,MinNumOfItem,MaxNumOfItem,MinNumFromCategory,MaxNumFromCategory
    public String params; //Price "<productId>" ,Item "<prodId> <quantity>" ,Category "<category> <quantity>"
    public composore composore; // And, Or, Xor for more than one predicate
}
