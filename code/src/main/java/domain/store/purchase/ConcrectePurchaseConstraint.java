package domain.store.purchase;

import utils.Order;

public class ConcrectePurchaseConstraint extends PurchaseConstraint{
    @Override
    public boolean handle(Order order){
        return true;
    }

}
