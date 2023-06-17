package domain.states;


import database.Dao;
import domain.store.storeManagement.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.LinkedList;
import java.util.List;

@Entity
public class StoreManager extends UserState {

    @Transient
    private List<Action> actions;

    @Transient
    private List<Action> addedActions;

    public StoreManager(){
        actions = new LinkedList<>();
        addedActions = new LinkedList<>();

        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.seeStoreOrders);
        actions.add(Action.checkWorkersStatus);

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
    }

    public  StoreManager(int memberId, String name, Store store){
        super(memberId, name, store);
        role = Role.Manager;
        actions = new LinkedList<>();
        addedActions = new LinkedList<>();


        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);
        actions.add(Action.seeStoreOrders);
        actions.add(Action.checkWorkersStatus);

        for(Action a : actions)
            Dao.save(new Permission(userId, storeId, a));
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
                Dao.save(new Permission(userId, storeId, a));
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
            Dao.removeIf(Permission.class,"Permission", String.format("permission = '%s'", a.toString()));
        }
        else
            throw new Exception("the manager does not have this action");
    }

    @Override
    protected void getPermissionsFromDb() {
        getPermissionsHelp();
        for(Action a : addedActions)
            if(!permissions.checkPermission(a))
                permissions.addPossibleAction(a);
    }
}
