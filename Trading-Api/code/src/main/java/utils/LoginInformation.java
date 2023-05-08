package utils;

import utils.stateRelated.Role;

import java.util.HashMap;
import java.util.List;

public class LoginInformation {
    private String token;
    private int userId;
    private String userName;
    private boolean isAdmin;
    private List<String> notifications;
    private boolean hasQuestions;
    private HashMap<Integer, Role> storeRoles;
    //private HashMap<Integer, String> storeNames;

    public LoginInformation(String t, int ui, String un, boolean isAdmin, List<String> notifications, boolean hasQuestions, HashMap<Integer,
            Role> storeRoles){
        token=t;
        userId = ui;
        userName=un;
        this.isAdmin = isAdmin;
        this.notifications = notifications;
        this.hasQuestions = hasQuestions;
        this.storeRoles = storeRoles;
        //TODO: this.storeNames = storeNames; add name to store
    }
    public int getUserId(){
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }
}
