package domain.states;

import domain.store.storeManagement.Store;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.LinkedList;
import java.util.List;

public class StoreOwner extends UserState {

    public  StoreOwner(int userId, String name, Store store){
        super(userId, name, store);
        role = Role.Owner;
        List<Action> actions = new LinkedList<>();

        actions.add(Action.appointManager);
        actions.add(Action.changeStoreDetails);
        actions.add(Action.changePurchasePolicy);
        actions.add(Action.changeDiscountPolicy);
        actions.add(Action.addPurchaseConstraint);
        actions.add(Action.fireManager);
        actions.add(Action.checkWorkersStatus);
        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.seeStoreOrders);
        actions.add(Action.addProduct);
        actions.add(Action.removeProduct);
        actions.add(Action.updateProduct);
        actions.add(Action.addDiscountConstraint);

        actions.add(Action.appointOwner);
        actions.add(Action.fireOwner);
        actions.add(Action.changeManagerPermission);

        permission.addActions(actions);
    }
}
