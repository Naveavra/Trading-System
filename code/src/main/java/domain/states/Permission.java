package domain.states;

import utils.Action;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Permission {
    ConcurrentLinkedDeque<Action> actions;

    public Permission(){
        actions = new ConcurrentLinkedDeque<>();

    }

    public boolean checkPermission(Action a){
        return actions.contains(a);
    }

    public void addActions(List<Action> actions){

    }

    public void removeAction(Action a){

    }
}
