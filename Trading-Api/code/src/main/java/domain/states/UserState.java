package domain.states;

import domain.store.storeManagement.Store;
import domain.user.Member;
import market.Market;
import org.json.JSONObject;
import utils.infoRelated.Information;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.List;
import java.util.Set;

public abstract class UserState extends Information {

    protected int userId;
    protected String userName;
    protected Store store;
    protected Permission permission; //saves all the permission a user has for a store.
    protected Role role;
    protected boolean isActive;

    public UserState(int userId, String name, Store store){
        this.userId = userId;
        this.userName = name;
        this.store = store;
        permission = new Permission();
        isActive = true;

    }


    public Store getStore(){return store;}
    public void setIsActive(boolean isActive){
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

    public Store appointManager(Member appointed) throws Exception{
        checkPermission(Action.appointManager);
        store.appointUser(userId, appointed, new StoreManager(appointed.getId(), appointed.getName(), store));
        return store;
    }

    public void fireManager(int appointedId) throws Exception{
        if(userId == appointedId)
            store.fireUser(appointedId);
        checkPermission(Action.fireManager);
        store.fireUser(appointedId);
    }

    public Store appointOwner(Member appointed) throws Exception{
        checkPermission(Action.appointOwner);
        store.appointUser(userId, appointed, new StoreOwner(appointed.getId(), appointed.getName(), store));
        return store;
    }

    public Set<Integer> fireOwner(int appointedId) throws Exception{
        if(userId == appointedId)
            return store.fireUser(appointedId);
        checkPermission(Action.fireOwner);
        return store.fireUser(appointedId);
    }

    public Set<Integer> closeStore() throws Exception{
        checkPermission(Action.closeStore);
        //setIsActive(false);
        return store.closeStoreTemporary(userId);
    }

    public Set<Integer> reOpenStore() throws Exception{
        checkPermission(Action.reopenStore);
        //setIsActive(true);
        return store.reopenStore(userId);
    }

    public Set<Integer> getWorkerIds() throws Exception{
        checkPermission(Action.checkWorkersStatus);
        return store.getUsersInStore();
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("userName", userName);
        json.put("storeRole", role.toString());
        json.put("actions", fromActionToString(getActions()));
        return json;
    }
}
