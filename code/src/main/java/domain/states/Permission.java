package domain.states;

import utils.Action;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Permission {
    private ConcurrentLinkedDeque<Action> actions;
    private List<Action> addedActions;

    public Permission(){
        actions = new ConcurrentLinkedDeque<>();
        addedActions = new LinkedList<>();

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
}
