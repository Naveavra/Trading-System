package data;

import utils.stateRelated.Action;

import java.util.List;

public class PermissionInfo {

    List<Action> actions;

    public PermissionInfo(List<Action> actions){
        this.actions = actions;
    }

    public List<Action> getActions(){
        return actions;
    }

    public int size(){
        return actions.size();
    }
}
