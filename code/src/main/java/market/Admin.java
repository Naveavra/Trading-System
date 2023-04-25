package market;

import service.MarketController;
import service.UserController;
import utils.Notification;

import java.util.List;
import java.util.Set;

public class Admin {

    private int adminId;

    private String emailAdmin;
    private String passwordAdmin;

    private MarketController marketController;
    private UserController userController;
    public Admin(int adminId, String email, String password){
        this.adminId = adminId;
        emailAdmin = email;
        passwordAdmin = password;

    }
    public int getAdminId(){
        return adminId;
    }
    public void closeStorePermanently(int storeId) throws Exception {
        Set<Integer> userIds = marketController.closeStorePermanently(storeId);
        for(int userId : userIds){
            String notify = "the store: " + storeId +" has been permanently closed";
            Notification<String> notification = new Notification<>(notify);
            userController.addNotification(userId, notification);
            userController.removeStoreRole(adminId, userId, storeId);
        }
    }
    public boolean checkEmail(String email){
        return emailAdmin == email;
    }
    public boolean checkPassword(String pass){
        return passwordAdmin == pass;
    }
    public String



    public void addControllers(UserController userController, MarketController marketController) {
        this.marketController = marketController;
        this.userController = userController;
    }
}
