package domain.user;

import utils.Role;

import java.util.LinkedList;
import java.util.List;

public class AppHistory {

    public List<AppHistory> appointed;
    public Role role;
    public int appointedUserId;
    public AppHistory(int appointedUserId, Role role){
        this.appointedUserId = appointedUserId;
        this.role = role;
        appointed = new LinkedList<>();
    }

    //in case a user appoints another user to the store
    public void addAppointed(int appointee, int appointed, Role role){

    }

    //remove the manager and all of his appointed if needed
    public void removeAppointed(int appointed){

    }
}
