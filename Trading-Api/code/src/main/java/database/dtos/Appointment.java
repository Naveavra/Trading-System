package database.dtos;

import database.DbEntity;
import database.daos.Dao;
import database.daos.StoreDao;
import database.daos.SubscriberDao;
import domain.states.StoreManager;
import domain.states.StoreOwner;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import domain.user.Member;
import jakarta.persistence.*;
import org.json.JSONObject;
import utils.infoRelated.Information;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;
import utils.stateRelated.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "appointments")
public class Appointment extends Information implements DbEntity {

    @Id
    private int storeId;
    @Transient
    private Store store;
    private int fatherId;
    @Id
    private String fatherName;
    @Id
    private String childName;
    @Transient
    private Member child;
    private Role role;
    @Transient
    private HashMap<String, Boolean> approvers;

    private boolean approved;


    public Appointment() {
    }
    public Appointment(Store store, int fatherId, String fatherName, Member child, Role role, List<String> appointers) {
        this.store = store;
        this.storeId = store.getStoreId();
        this.fatherId = fatherId;
        this.fatherName = fatherName;
        this.child = child;
        this.childName = child.getName();
        this.role = role;
        approved = false;
        this.approvers = new HashMap<>();
        Dao.save(new AppApproved(storeId, fatherId, getChildId(), fatherName, true));
        approvers.put(fatherName, true);
        for(String name: appointers)
            if(!name.equals(fatherName)) {
                Dao.save(new AppApproved(storeId, fatherId, getChildId(), name, false));
                approvers.put(name, false);
            }
        checkAllApproved();
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getFatherId(){
        return fatherId;
    }
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }


    public int getChildId(){
        return child.getId();
    }
    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public boolean getApproved(){
        return approved;
    }

    public void approve(String name) throws Exception {
        if(approved)
            throw new Exception("the appointment was already approved");
        if(!approvers.containsKey(name))
            throw new Exception("the name isn't allowed to approve this appointment");
        if(approvers.get(name))
            throw new Exception("the user already approved this appointment");
        Dao.save(new AppApproved(storeId, fatherId, getChildId(), name, true));
        approvers.put(name, true);
        checkAllApproved();
    }

    public boolean canDeny(String name) throws Exception{
        if(!approvers.containsKey(name))
            return false;
        if(approvers.get(name))
            return false;
        return true;
    }
    public void checkAllApproved() {
        boolean ans = true;
        for(boolean approved : approvers.values())
            ans = ans && approved;
        if(ans){
            UserState state = null;
            try {
                if (role == Role.Manager)
                    state = new StoreManager(getChildId(), childName, store);
                else if (role == Role.Owner)
                    state = new StoreOwner(getChildId(), childName, store);
                store.appointUser(fatherId, child, state);
                child.changeRoleInStore(state, store);
                Notification notify = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA,
                        String.format("you have been appointed to %s in store: %d", role.toString(), storeId));
                child.addNotification(notify);
                approved = true;
            }catch (Exception ignored){}
        }
    }

    public boolean containsInApprove(String name){
        for(String userName : approvers.keySet())
            if(userName.equals(name))
                return true;
        return false;
    }

    public void removeApprover(String name) {
        approvers.remove(name);
        Dao.removeIf("AppApproved", String.format("storeId = %d AND approverName = '%s' ", storeId, name));
    }
    public List<String> getApprovedNames() {
        List<String> ans = new ArrayList<>();
        for(String name : approvers.keySet())
            if(approvers.get(name))
                ans.add(name);
        return ans;
    }

    public List<String> getNotApproved() {
        List<String> ans = new ArrayList<>();
        for(String name : approvers.keySet())
            if(!approvers.get(name))
                ans.add(name);
        return ans;
    }
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("storeId", storeId);
        json.put("memane", fatherName);
        json.put("memune", childName);
        json.put("role", role.toString());
        json.put("approved", getApprovedNames());
        json.put("notApproved", getNotApproved());
        json.put("status", approved);
        return json;
    }

    @Override
    public void initialParams() {
        getStoreFromDb();
        getChildFromDb();
        getApproversFromDb();
    }

    private void getStoreFromDb(){
        if(store == null)
            store = StoreDao.getStore(storeId);
    }

    private void getChildFromDb(){
        if(child == null)
            child = SubscriberDao.getMember(childName);
    }

    private void getApproversFromDb(){
        if(approvers == null) {
            approvers = new HashMap<>();
            List<? extends DbEntity> approversDb = Dao.getByParamList(AppApproved.class, "AppApproved",
                    String.format("storeId = %d AND fatherId = %d AND childId = %d", storeId, fatherId, child.getId()));
            for (AppApproved appApproved : (List<AppApproved>) approversDb)
                approvers.put(appApproved.getApproverName(), appApproved.isApproved());
        }

    }

}
