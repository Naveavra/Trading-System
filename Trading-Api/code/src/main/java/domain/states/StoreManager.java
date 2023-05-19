package domain.states;


import domain.store.storeManagement.Store;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.LinkedList;
import java.util.List;

public class StoreManager extends UserState {

    public  StoreManager(int userId, Store store){
        super(userId, store);
        role = Role.Manager;
        List<Action> actions = new LinkedList<>();
        List<Action> addedActions = new LinkedList<>();


        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.seeStoreOrders);
        actions.add(Action.checkWorkersStatus);
        permission.addActions(actions);


        addedActions.add(Action.viewMessages);
        addedActions.add(Action.answerMessage);
        addedActions.add(Action.seeStoreHistory);
        addedActions.add(Action.seeStoreOrders);
        addedActions.add(Action.checkWorkersStatus);
        addedActions.add(Action.changeStoreDetails);
        addedActions.add(Action.changePurchasePolicy);
        addedActions.add(Action.changeDiscountPolicy);
        addedActions.add(Action.addPurchaseConstraint);
        addedActions.add(Action.addDiscountConstraint);
        addedActions.add(Action.addProduct);
        addedActions.add(Action.removeProduct);
        addedActions.add(Action.updateProduct);
        permission.addPossibleActions(addedActions);
    }

    @Override
    public void addAction(Action a) throws Exception{
        if (!checkPermission(a)) {
            if (checkHasAvailableAction(a)) {
                permission.addAction(a);
            }
            else
                throw new Exception("manager can't have this action");
        }
        else
            throw new Exception("the manager already has this action");
    }

    @Override
    public void removeAction(Action a) throws Exception{
        if (checkPermission(a))
            permission.removeAction(a);
        else
            throw new Exception("the manager does not have this action");
    }
}
