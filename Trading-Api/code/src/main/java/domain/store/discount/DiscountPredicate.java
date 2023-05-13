package domain.store.discount;

import utils.orderRelated.Order;

public abstract class DiscountPredicate {
    //enum for predicates' composore
    public enum composore{And,Or,Xor}
    //enum of predicate types
    public enum PredicateTypes {MinPrice,MaxPrice,MinNumOfItem,MaxNumOfItem,MinNumFromCategory,MaxNumFromCategory}
    //shared params
    public int storeId;
    public DiscountPredicate next; // should be set with the enum type of composure
    public composore composoreType;
    public abstract boolean checkPredicate(Order order);

    public void setNext(DiscountPredicate pred, composore comp){
//        this.type = comp;
        DiscountPredicate temp = this;
        while(temp.next!=null){
            temp = temp.next;
        }
        temp.next = pred;
        temp.setComp(comp);
    }

    public boolean handleNext(Order order, boolean answer) {
        if(next == null)
            return answer;
        switch (composoreType){
            case Or -> {
                return (next.checkPredicate(order) || answer);
            }
            case And ->{
                return (next.checkPredicate(order) && answer);
            }
            case Xor ->{
                return (next.checkPredicate(order) ^ answer);
            }
            default -> {
                return answer;
            }
        }
    }

    public abstract boolean handleMax(double quantity);
    public abstract boolean handleMin(double quantity);
    private void setComp(composore comp){
        this.composoreType = comp;
    }
}
