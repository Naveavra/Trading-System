package domain.states;

import utils.stateRelated.Action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Permission {
    private List<Action> actions;
    private List<Action> addedActions;
    public static HashMap<Integer, Action> actionIds;
    public static HashMap<Action, Integer> actionsMap;

    public Permission(){
        actions = new LinkedList<>();
        addedActions = new LinkedList<>();
        actionIds = new HashMap<>();
        setActionIds();
        actionsMap = new HashMap<>();
        setActionsMap();

    }

    public boolean checkPermission(Action a){
        return actions.contains(a);
    }
    public boolean checkAvailablePermission(Action a){
        return addedActions.contains(a);
    }

    public void addActions(List<Action> actions){
        this.actions.addAll(actions);

    }
    public void addPossibleActions(List<Action> actions){
        addedActions.addAll(actions);
    }

    public void addAction(Action a){
        actions.add(a);
    }

    public void removeAction(Action a){
        actions.remove(a);
    }

    public List<Action> getActions() {
        return actions;
    }

    public static HashMap<Integer, Action> getActionIds(){
        if(actionIds == null)
        {
            actionIds = new HashMap<>();
            setActionIds();
        }
        return actionIds;
    }

    public static void setActionIds(){
        actionIds.put(0, Action.addProduct);
        actionIds.put(1, Action.removeProduct);
        actionIds.put(2, Action.updateProduct);
        actionIds.put(3, Action.changeStoreDetails);
        actionIds.put(4, Action.changePurchasePolicy);
        actionIds.put(5, Action.changeDiscountPolicy);
        actionIds.put(6, Action.addPurchaseConstraint);
        actionIds.put(7, Action.addDiscountConstraint);
        actionIds.put(8, Action.viewMessages);
        actionIds.put(9, Action.answerMessage);
        actionIds.put(10, Action.seeStoreHistory);
        actionIds.put(11, Action.seeStoreOrders);
        actionIds.put(12, Action.checkWorkersStatus);
        actionIds.put(13, Action.appointManager);
        actionIds.put(14, Action.fireManager);
        actionIds.put(15, Action.appointOwner);
        actionIds.put(16, Action.fireOwner);
        actionIds.put(17, Action.changeManagerPermission);
        actionIds.put(18, Action.closeStore);
        actionIds.put(19, Action.reopenStore);
    }

    public static HashMap<Action, Integer> getActionsMap(){
        if(actionIds == null)
        {
            actionsMap = new HashMap<>();
            setActionsMap();
        }
        return actionsMap;
    }
    private static void setActionsMap() {
        for(int id : actionIds.keySet())
            actionsMap.put(actionIds.get(id), id);
    }
}
