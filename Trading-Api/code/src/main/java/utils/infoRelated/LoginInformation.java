package utils.infoRelated;

import domain.store.storeManagement.Bid;
import domain.user.PurchaseHistory;
import org.json.JSONObject;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.*;

public class LoginInformation extends Information{
    private String token;
    private int userId;
    private String userName;
    private boolean isAdmin;
    private List<Notification> notifications;
    private HashMap<Integer, Role> storeRoles;
    private HashMap<Integer, String> storeNames;
    private HashMap<Integer, String> storeImg;
    private HashMap<Integer, List<Action>> permissions;
    private PurchaseHistory purchaseHistory;
    private int age;
    private String birthday;

    private List<Bid> openBids;

    public LoginInformation(String t, int ui, String un, boolean isAdmin, List<Notification> notifications, HashMap<Integer, Role> storeRoles,
                            HashMap<Integer, String> storeName, HashMap<Integer, String> storeImg,
                            HashMap<Integer, List<Action>> permissions, PurchaseHistory purchaseHistory,
                            int age, String birthday, List<Bid> bids){
        token=t;
        userId = ui;
        userName=un;
        this.isAdmin = isAdmin;
        this.notifications = notifications;
        this.storeRoles = storeRoles;
        this.storeNames = storeName;
        this.storeImg = storeImg;
        this.permissions = permissions;
        this.purchaseHistory = purchaseHistory;
        this.age = age;
        this.birthday = birthday;
        this.openBids = bids;
    }
    public int getUserId(){
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;}
    public boolean getIsAdmin(){return isAdmin;}
    public List<Notification> getNotifications(){return notifications;}
    public HashMap<Integer, Role> getStoreRoles(){return storeRoles;}
    public HashMap<Integer, String> getStoreNames(){return storeNames;}
    public HashMap<Integer, String> getStoreImg(){return storeImg;}

    public HashMap<Integer, List<Action>> getPermissions() {
            return permissions;
    }

    public List<Bid> getOpenBids(){return openBids;}


    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("token", getToken());
        json.put("userId", getUserId());
        json.put("userName", getUserName());
        json.put("isAdmin", getIsAdmin());
        json.put("notifications", infosToJson(getNotifications()));
        if(!isAdmin) {
            json.put("purchaseHistory", infosToJson(purchaseHistory.getReceipts()));
            json.put("age", age);
            json.put("birthday", birthday);
            json.put("storeNames", toJsonHM(getStoreNames(), "storeId", "storeName"));
            json.put("storeRoles", getStoreRole(getStoreRoles(), "storeId", "storeRole"));
            json.put("storeImgs", toJsonHM(getStoreImg(), "storeId", "storeImg"));
            json.put("permissions", getStorePermissions(getPermissions(), "storeId", "actions"));
            json.put("bids", infosToJson(getOpenBids()));
        }
        return json;
    }

    private List<JSONObject> toJsonHM(HashMap<Integer, String> hashMap, String key, String value){
        List<JSONObject> jsonList = new ArrayList();
        if(hashMap != null)
        {
            for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
                JSONObject jsonO = new JSONObject();
                jsonO.put(key, entry.getKey());
                jsonO.put(value, entry.getValue());
                jsonList.add(jsonO);
            }
        }
        return jsonList;
    }

    private List<JSONObject> getStoreRole(HashMap<Integer, Role> hashMap, String key, String value){
        List<JSONObject> jsonList = new ArrayList();
        if(hashMap != null) {
            for (Map.Entry<Integer, Role> entry : hashMap.entrySet()) {
                JSONObject jsonO = new JSONObject();
                jsonO.put(key, entry.getKey());
                jsonO.put(value, entry.getValue());
                jsonList.add(jsonO);
            }
        }
        return jsonList;
    }
    private List<JSONObject> getStorePermissions(HashMap<Integer, List<Action>> hashmap, String key, String value){
        List<JSONObject> jsonList = new ArrayList();
        if(hashmap != null) {
            for (Map.Entry<Integer, List<Action>> entry : hashmap.entrySet()) {
                JSONObject jsonO = new JSONObject();
                jsonO.put(key, entry.getKey());
                jsonO.put(value, fromActionToString(entry.getValue()));
                jsonList.add(jsonO);
            }
        }
        return jsonList;
    }
}
