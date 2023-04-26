package domain.states;


import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.LinkedList;
import java.util.List;

public class StoreManager extends UserState {

    public  StoreManager(){
        role = Role.Manager;
        permission = new Permission();
        List<Action> actions = new LinkedList<>();
        List<Action> addedActions = new LinkedList<>();


        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.seeStoreOrders);
        permission.addActions(actions);


        addedActions = new LinkedList<>();
        addedActions.add(Action.changeStoreDescription);
        addedActions.add(Action.changePurchasePolicy);
        addedActions.add(Action.changeDiscountPolicy);
        addedActions.add(Action.addPurchaseConstraint);
        addedActions.add(Action.addDiscountConstraint);
        addedActions.add(Action.checkWorkersStatus);
        addedActions.add(Action.addProduct);
        permission.addPossibleActions(addedActions);
    }
}
