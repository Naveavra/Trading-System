package domain.states;

import database.daos.Dao;
import database.DbEntity;
import database.daos.StoreDao;
import database.dtos.Appointment;
import domain.store.storeManagement.Store;
import domain.user.Member;
import jakarta.persistence.*;
import org.hibernate.Session;
import org.json.JSONObject;
import utils.infoRelated.Information;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "roles")
public abstract class UserState extends Information implements DbEntity {


    @Id
    protected int userId;

    @Id
    protected int storeId;
    @Transient
    protected Store store;
    protected String userName;
    @Transient
    protected Permissions permissions; //saves all the permission a user has for a store.

    @Enumerated(EnumType.STRING)
    protected Role role;

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
        isActive = true;


    }

    public List<Action> getActions() {
        return permissions.getActions();
    }
    public List<Action> getPossibleActions(){return permissions.getAddedActions();}


    public Store getStore(){return store;}

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

    public void addAction(Action a, Session session) throws Exception{
        throw new Exception("cannot add action to role: " + role);
    }

    public void removeAction(Action a, Session session) throws Exception{
        throw new Exception("cannot remove action to role: " + role);
    }

    public List<String> appointManager(Member appointed, Session session) throws Exception{
        checkPermission(Action.appointManager);
        List<String> approvers = new ArrayList<>(store.getAppHistory().getStoreWorkersWithPermission(Action.appointManager));
        store.canAppointUser(userId, appointed, Role.Manager);
        Appointment appointment = new Appointment(store, userId, userName, appointed, Role.Manager, approvers, session);
        Dao.save(appointment, session);
        store.addAppointment(appointment, session);
        return approvers;
    }

    public Set<Integer> fireManager(int appointedId, Session session) throws Exception{
        if(userId == appointedId)
            return store.fireUser(appointedId, session);
        checkPermission(Action.fireManager);
        return store.fireUser(appointedId, session);
    }

    public List<String> appointOwner(Member appointed, Session session) throws Exception{
        checkPermission(Action.appointOwner);
        store.canAppointUser(userId, appointed, Role.Owner);
        List<String> approvers = new ArrayList<>(store.getAppHistory().getStoreWorkersWithPermission(Action.appointOwner));
        Appointment appointment = new Appointment(store, userId, userName, appointed, Role.Owner, approvers, session);
        Dao.save(appointment, session);
        store.addAppointment(appointment, session);
        return approvers;
    }

    public Set<Integer> fireOwner(int appointedId, Session session) throws Exception{
        if(userId == appointedId)
            return store.fireUser(appointedId, session);
        checkPermission(Action.fireOwner);
        return store.fireUser(appointedId, session);
    }

    public Set<Integer> closeStore(Session session) throws Exception{
        checkPermission(Action.closeStore);
        Set<Integer> ans = store.closeStoreTemporary(userId);
        Dao.save(store, session);
        return ans;
    }

    public Set<Integer> reOpenStore(Session session) throws Exception{
        checkPermission(Action.reopenStore);
        Set<Integer> ans = store.reopenStore(userId);
        Dao.save(store, session);
        return ans;
    }

    public Set<Integer> getWorkerIds() throws Exception{
        checkPermission(Action.checkWorkersStatus);
        return store.getUsersInStore();
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

    public void saveStatePermissions(Session session) throws Exception{
        for(Action a : permissions.getActions())
            Dao.save(new Permission(userId, storeId, a), session);
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

    @Override
    public void initialParams() throws Exception{
        getStoreFromDb();
        getPermissionsFromDb();
    }

    protected void getStoreFromDb() throws Exception{
        if(store == null){
            store = StoreDao.getStore(storeId);
        }
    }

    protected abstract void getPermissionsFromDb() throws Exception;

    protected void getPermissionsHelp() throws Exception{
        if(permissions == null) {
            permissions = new Permissions();
            List<? extends DbEntity> permissionsDto = Dao.getListByCompositeKey(Permission.class, userId, storeId,
                    "Permission", "userId", "storeId");
            for (Permission p : (List<Permission>) permissionsDto)
                permissions.addAction(p.getPermission());
        }
    }
}
