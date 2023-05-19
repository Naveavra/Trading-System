package domain.states;

import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.List;

public abstract class UserState {
    protected Permission permission; //saves all the permission a user has for a store.
    protected Role role;
    protected boolean isActive;

    public UserState(){
        permission = new Permission();
        isActive = true;

    }

    public void serIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public boolean isActive(){
        return isActive;
    }
    public boolean checkPermission(Action a){
        return permission.checkPermission(a);
    }
    public Role getRole(){
        return role;
    }

    public boolean checkHasAvailableAction(Action a){
        return permission.checkAvailablePermission(a);
    }

    public void addAction(Action a) throws Exception{
        throw new Exception("cannot add action to role: " + role);
    }

    public void removeAction(Action a) throws Exception{
        throw new Exception("cannot remove action to role: " + role);
    }

    public List<Action> getActions() {
        return permission.getActions();
    }
}
