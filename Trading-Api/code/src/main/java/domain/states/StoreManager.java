package domain.states;


import database.DbEntity;
import database.daos.Dao;
import domain.store.storeManagement.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.hibernate.Session;
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
        addedActions.add(Action.deletePurchasePolicy);
        addedActions.add(Action.deleteDiscountPolicy);
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

        permissions.addActions(actions);


        addedActions.add(Action.viewMessages);
        addedActions.add(Action.answerMessage);
        addedActions.add(Action.seeStoreHistory);
        addedActions.add(Action.seeStoreOrders);
        addedActions.add(Action.checkWorkersStatus);
        addedActions.add(Action.changeStoreDetails);
        addedActions.add(Action.deletePurchasePolicy);
        addedActions.add(Action.deleteDiscountPolicy);
        addedActions.add(Action.addPurchaseConstraint);
        addedActions.add(Action.addDiscountConstraint);
        addedActions.add(Action.addProduct);
        addedActions.add(Action.removeProduct);
        addedActions.add(Action.updateProduct);
        permissions.addPossibleActions(addedActions);
    }

    @Override
    public void addAction(Action a, Session session) throws Exception{
        if (!checkPermission(a)) {
            if (checkHasAvailableAction(a)) {
                Dao.save(new Permission(userId, storeId, a), session);
                permissions.addAction(a);
            }
            else
                throw new Exception("manager can't have this action");
        }
        else
            throw new Exception("the manager already has this action");
    }

    @Override
    public void removeAction(Action a, Session session) throws Exception{
        if (checkPermission(a)) {
            permissions.removeAction(a);
            Dao.removeIf("Permission", String.format("permission = '%s'", a.toString()), session);
        }
        else
            throw new Exception("the manager does not have this action");
    }

    @Override
    protected void getPermissionsHelp() throws Exception{
        if(permissions == null) {
            permissions = new Permissions();
            List<? extends DbEntity> permissionsDto = Dao.getListByCompositeKey(Permission.class, userId, storeId,
                    "Permission", "userId", "storeId");
            for (Permission p : (List<Permission>) permissionsDto)
                permissions.addAction(p.getPermission());
        }
    }

    @Override
    protected void getPermissionsFromDb() throws Exception{
        getPermissionsHelp();
        for(Action a : addedActions)
            if(!permissions.checkPermission(a))
                permissions.addPossibleAction(a);
    }
}
