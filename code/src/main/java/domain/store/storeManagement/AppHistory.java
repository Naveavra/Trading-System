package domain.store.storeManagement;

import utils.Role;

import java.util.LinkedList;
import java.util.List;

public class AppHistory {
    int creatorID;
    int storeID;
    public AppHistory(int creatorID, int storeID)
    {
        this.creatorID = creatorID;
        this.storeID = storeID;

    }

//    public List<AppHistory> appointed;
//    public AppHistory appointedMe;
//    public Role role;
//    public int appointedUserId;
//    public AppHistory(AppHistory appointedMe, int appointedUserId, Role role){
//        this.appointedUserId = appointedUserId;
//        this.role = role;
//        appointed = new LinkedList<>();
//    }
//
//    //in case a user appoints another user to the store
//    public void addAppointed(int appointee, int appointed, Role role){
//
//    }
//
//    //remove the manager and all of his appointed if needed
//    public void removeAppointed(int appointed){
//
//    }
}
