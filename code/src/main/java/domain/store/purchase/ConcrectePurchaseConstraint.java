package domain.store.purchase;

import utils.orderRelated.Order;

public class ConcrectePurchaseConstraint extends PurchaseConstraint{
    @Override
    public boolean handle(Order order){
        return true;
    }

}
