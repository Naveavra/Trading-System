package domain.states;

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

    @Transient
    protected Member member;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "storeId", foreignKey = @ForeignKey, referencedColumnName = "storeId"),
            @JoinColumn(name = "userId", foreignKey = @ForeignKey, referencedColumnName = "creatorId")
    })
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

    public UserState(Member member, String name, Store store){
        this.member = member;
        this.userName = name;
        this.store = store;
        permissions = new Permissions();
        permissionList = new ArrayList<>();
        isActive = true;


    }

    public List<Action> getActions() {
        return permissions.getActions();
    }
    public List<Action> getPossibleActions(){return permissions.getAddedActions();}


    public Store getStore(){return store;}
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
        StoreManager m = new StoreManager(appointed, appointed.getName(), store);
        store.appointUser(member.getId(), appointed, m);
        appointed.changeRoleInStore(m, store);
    }

    public Set<Integer> fireManager(int appointedId) throws Exception{
        if(member.getId() == appointedId)
            return store.fireUser(appointedId);
        checkPermission(Action.fireManager);
        return store.fireUser(appointedId);
    }

    public void appointOwner(Member appointed) throws Exception{
        checkPermission(Action.appointOwner);
        StoreOwner s = new StoreOwner(appointed, appointed.getName(), store);
        store.appointUser(member.getId(), appointed, s);
        appointed.changeRoleInStore(s, store);
    }

    public Set<Integer> fireOwner(int appointedId) throws Exception{
        if(member.getId() == appointedId)
            return store.fireUser(appointedId);
        checkPermission(Action.fireOwner);
        return store.fireUser(appointedId);
    }

    public Set<Integer> closeStore() throws Exception{
        checkPermission(Action.closeStore);
        //setIsActive(false);
        return store.closeStoreTemporary(member.getId());
    }

    public Set<Integer> reOpenStore() throws Exception{
        checkPermission(Action.reopenStore);
        //setIsActive(true);
        return store.reopenStore(member.getId());
    }

    public Set<Integer> getWorkerIds() throws Exception{
        checkPermission(Action.checkWorkersStatus);
        return store.getUsersInStore();
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("userId", member);
        json.put("userName", userName);
        json.put("storeRole", role.toString());
        json.put("actions", fromActionToString(getActions()));
        return json;
    }
}
