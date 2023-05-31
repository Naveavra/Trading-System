package domain.store.discount.predicates;

import utils.orderRelated.Order;

public class DiscountItemPredicate extends DiscountPredicate{
    public PredicateTypes type;
    public int prodId; // used by item predicate.
    public int quantity; //used by item predicate, this is the quantity for an item needs to uphold the type.

    public DiscountItemPredicate(int productId,int quantity,PredicateTypes type,int storeId){
        this.type = type;
        this.prodId = productId;
        this.quantity = quantity;
        this.storeId = storeId;
    }

    @Override
    public boolean checkPredicate(Order order) {
        boolean answer = false;
        int quantity = order.getShoppingCart().getBasket(storeId).getProduct(prodId).getQuantity();
        if(quantity!=0){
            switch (type){
                case MinNumOfItem ->answer = handleMin(quantity);
                case MaxNumOfItem -> answer = handleMax(quantity);
            }
            return handleNext(order,answer);
        }
        return answer;
    }

    @Override
    public boolean handleMax(double quantity) {
        return this.quantity<=quantity;
    }

    @Override
    public boolean handleMin(double quantity) {
        return this.quantity>=quantity;
    }
}
