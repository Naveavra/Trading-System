package domain.states;

import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.List;

public abstract class UserState {
    protected Permission permission; //saves all the permission a user has for a store.
    protected Role role;
    public boolean checkPermission(Action a){
        return permission.checkPermission(a);
    }
    public Role getRole(){
        return role;
    }

    public boolean checkHasAvailableAction(Action a){
        return permission.checkAvailablePermission(a);
    }

    public void addAction(Action a){
        permission.addAction(a);
    }

    public void removeAction(Action a){
        permission.removeAction(a);
    }

    public List<Action> getActions() {
        return permission.getActions();
    }
}
