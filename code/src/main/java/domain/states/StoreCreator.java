package domain.states;

import utils.Action;
import utils.Role;

import java.util.LinkedList;
import java.util.List;

public class StoreCreator extends UserState {

    public StoreCreator() {
        role = Role.Creator;
        permission = new Permission();
        List<Action> actions = new LinkedList<>();


        actions.add(Action.appointManager);
        actions.add(Action.changeStoreDescription);
        actions.add(Action.changePurchasePolicy);
        actions.add(Action.changeDiscountPolicy);
        actions.add(Action.addPurchaseConstraint);
        actions.add(Action.fireManager);
        actions.add(Action.checkWorkersStatus);
        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.addProduct);
        actions.add(Action.addDiscountConstraint);

        actions.add(Action.addOwner);
        actions.add(Action.fireOwner);
        actions.add(Action.changeManagerPermission);

        actions.add(Action.closeStore);
        actions.add(Action.reopenStore);

        permission.addActions(actions);
    }
}