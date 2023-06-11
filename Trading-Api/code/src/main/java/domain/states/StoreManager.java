package domain.states;


import domain.store.storeManagement.Store;
import domain.user.Member;
import jakarta.persistence.Entity;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.LinkedList;
import java.util.List;

@Entity
public class StoreManager extends UserState {

    public StoreManager(){
    }

    public  StoreManager(Member member, String name, Store store){
        super(member, name, store);
        role = Role.Manager;
        List<Action> actions = new LinkedList<>();
        List<Action> addedActions = new LinkedList<>();


        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.seeStoreOrders);
        actions.add(Action.checkWorkersStatus);

        for(Action a : actions)
            permissionList.add(new Permission(this, a));
        permissions.addActions(actions);


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
        permissions.addPossibleActions(addedActions);
    }

    @Override
    public void addAction(Action a) throws Exception{
        if (!checkPermission(a)) {
            if (checkHasAvailableAction(a)) {
                permissionList.add(new Permission(this, a));
                permissions.addAction(a);
            }
            else
                throw new Exception("manager can't have this action");
        }
        else
            throw new Exception("the manager already has this action");
    }

    @Override
    public void removeAction(Action a) throws Exception{
        if (checkPermission(a)) {
            permissions.removeAction(a);
            permissionList.removeIf(p -> a == p.getPermission());
        }
        else
            throw new Exception("the manager does not have this action");
    }
}
