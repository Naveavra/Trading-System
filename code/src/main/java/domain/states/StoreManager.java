package domain.states;


import utils.Action;
import utils.Role;

import java.util.LinkedList;
import java.util.List;

public class StoreManager extends UserState {

    private List<Action> addedActions;
    public  StoreManager(){
        role = Role.Manager;
        permission = new Permission();
        List<Action> actions = new LinkedList<>();

        /*
        actions.add(Action.buyProduct);
        actions.add(Action.createStore);
        actions.add(Action.getProductInformation);
        actions.add(Action.getStoreInformation);
        actions.add(Action.writeReview);
        actions.add(Action.rateProduct);
        actions.add(Action.rateStore);
        actions.add(Action.sendQuestion);
        actions.add(Action.sendComplaint);
        actions.add(Action.sellProduct);
         */

        actions.add(Action.viewMessages);
        actions.add(Action.answerMessage);
        actions.add(Action.seeStoreHistory);

        permission.addActions(actions);


        addedActions = new LinkedList<>();
        addedActions.add(Action.appointManager);
        addedActions.add(Action.changeStoreDescription);
        addedActions.add(Action.changePurchasePolicy);
        addedActions.add(Action.changeDiscountPolicy);
        addedActions.add(Action.addPurchaseConstraint);
        addedActions.add(Action.fireManager);
        addedActions.add(Action.checkWorkersStatus);
        addedActions.add(Action.addProduct);
        addedActions.add(Action.addDiscountConstraint);
    }

    public boolean actionCanBeAdded(Action a){
        return addedActions.contains(a);
    }

    public void addActionToManager(Action a) throws Exception {
        if(actionCanBeAdded(a))
            permission.addAction(a);
        else
            throw new Exception("this action can't be added to manager");

    }

    public void removeActionFromManager(Action a){
        permission.removeAction(a);
    }


}
