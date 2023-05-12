package domain.store.discount;

import utils.orderRelated.Order;

public abstract class DiscountPredicate {
    public String category; //used by category predicate
    public int minNumFromCategory; // used by category predicate
    public int basketPrice; // used by price predicate
    public int prodId; // used by item predicate
    public int minQuantity; //used by item predicate, this is the min quantity for an item.
    public DiscountPredicate next;

    public abstract boolean checkPredicate(Order order);
    public void setNext(DiscountPredicate pred){

    }
}
