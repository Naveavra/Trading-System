package domain.states;

import database.Dao;
import domain.store.storeManagement.Store;
import domain.user.Member;
import jakarta.persistence.*;
import org.json.JSONObject;
import utils.infoRelated.Information;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "roles")
public abstract class UserState extends Information {


    @Id
    protected int userId;

    @Id
    protected int storeId;
    @Transient
    protected Store store;
    protected String userName;
    @Transient
    protected Permissions permissions; //saves all the permission a user has for a store.
    @OneToMany(cascade = CascadeType.ALL, mappedBy="state")
    protected List<Permission> permissionList;

    @Enumerated(EnumType.STRING)
    protected Role role;
    @Transient
    protected boolean isActive;

    public UserState(){
    }

    public UserState(int memberId, String name, Store store){
        this.userId = memberId;
        this.userName = name;
        this.store = store;
        if(store != null)
            this.storeId = store.getStoreId();
        permissions = new Permissions();
        permissionList = new ArrayList<>();
        isActive = true;


    }

    public List<Action> getActions() {
        return permissions.getActions();
    }
    public List<Action> getPossibleActions(){return permissions.getAddedActions();}


    public Store getStore(){return getStoreFromDb();}

    public int getUserId(){return userId;}
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public boolean isActive(){
        return isActive;
    }
    public boolean checkPermission(Action a){
        return permissions.checkPermission(a);
    }
    public Role getRole(){
        return role;
    }

    public boolean checkHasAvailableAction(Action a){
        return permissions.checkAvailablePermission(a);
    }

    public void addAction(Action a) throws Exception{
        throw new Exception("cannot add action to role: " + role);
    }

    public void removeAction(Action a) throws Exception{
        throw new Exception("cannot remove action to role: " + role);
    }

    public void appointManager(Member appointed) throws Exception{
        checkPermission(Action.appointManager);
        StoreManager m = new StoreManager(appointed.getId(), appointed.getName(), getStoreFromDb());
        getStoreFromDb().appointUser(userId, appointed, m);
        appointed.changeRoleInStore(m, getStoreFromDb());
    }

    public Set<Integer> fireManager(int appointedId) throws Exception{
        if(userId == appointedId)
            return getStoreFromDb().fireUser(appointedId);
        checkPermission(Action.fireManager);
        return getStoreFromDb().fireUser(appointedId);
    }

    public void appointOwner(Member appointed) throws Exception{
        checkPermission(Action.appointOwner);
        StoreOwner s = new StoreOwner(appointed.getId(), appointed.getName(), getStoreFromDb());
        getStoreFromDb().appointUser(userId, appointed, s);
        appointed.changeRoleInStore(s, getStoreFromDb());
    }

    public Set<Integer> fireOwner(int appointedId) throws Exception{
        if(userId == appointedId)
            return getStoreFromDb().fireUser(appointedId);
        checkPermission(Action.fireOwner);
        return getStoreFromDb().fireUser(appointedId);
    }

    public Set<Integer> closeStore() throws Exception{
        checkPermission(Action.closeStore);
        //setIsActive(false);
        Set<Integer> ans = getStoreFromDb().closeStoreTemporary(userId);
        Dao.save(getStoreFromDb());
        return ans;
    }

    public Set<Integer> reOpenStore() throws Exception{
        checkPermission(Action.reopenStore);
        //setIsActive(true);
        Set<Integer> ans = getStoreFromDb().reopenStore(userId);
        Dao.save(getStoreFromDb());
        return ans;
    }

    public Set<Integer> getWorkerIds() throws Exception{
        checkPermission(Action.checkWorkersStatus);
        return getStoreFromDb().getUsersInStore();
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    public String getUserName(){
        return this.userName;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("userName", userName);
        json.put("storeRole", role.toString());
        json.put("actions", fromActionToString(getActions()));
        return json;
    }

    //database
    public Store getStoreFromDb(){
        if(store == null){
            store = (Store) Dao.getById(Store.class, storeId);
        }
        return store;
    }
}
