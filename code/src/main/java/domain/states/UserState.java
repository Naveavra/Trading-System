package domain.states;

import utils.Action;
import utils.Role;

public abstract class UserState {
    protected Permission permission; //saves all the permission a user has for a store.
    protected Role role;
    public boolean checkPermission(Action a){
        return permission.checkPermission(a);
    }
}
