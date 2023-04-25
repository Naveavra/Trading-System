package domain.store.purchase;

import utils.Order;

import java.util.concurrent.ConcurrentLinkedDeque;

public class PurchasePolicy {
    ConcurrentLinkedDeque<PurchaseConstraint> constraints;
    public PurchasePolicy(){
        constraints = new ConcurrentLinkedDeque<>();
    }
    public boolean handlePurchase(Order order){
        for(PurchaseConstraint constraint: constraints){
            if(constraint.handle(order) == false){
                return false;
            }
        }
        return true;
    }
    public boolean createConstraint(String constraint) {
        //TODO handle parsing the constraint (probably using Stanford NLP library)
        constraints.add(new ConcrectePurchaseConstraint());
        return true;
    }
}
