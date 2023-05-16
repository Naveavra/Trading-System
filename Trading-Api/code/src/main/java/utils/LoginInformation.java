package utils;

import utils.stateRelated.Role;

import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;

public class LoginInformation {
    private String token;
    private int userId;
    private String userName;
    private boolean isAdmin;
    private List<String> notifications;
    private boolean hasQuestions;
    private HashMap<Integer, Role> storeRoles;
    private HashMap<Integer, String> storeNames;
    private HashMap<Integer, String> storeImg;

    public LoginInformation(String t, int ui, String un, boolean isAdmin, List<String> notifications, boolean hasQuestions, HashMap<Integer,
            Role> storeRoles, HashMap<Integer, String> storeName, HashMap<Integer, String> storeImg){
        token=t;
        userId = ui;
        userName=un;
        this.isAdmin = isAdmin;
        this.notifications = notifications;
        this.hasQuestions = hasQuestions;
        this.storeRoles = storeRoles;
        this.storeNames = storeName;
        this.storeImg = storeImg;
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
