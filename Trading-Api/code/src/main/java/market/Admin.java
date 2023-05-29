package market;

import service.MarketController;
import service.UserController;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Admin {

    private int adminId;

    private String emailAdmin;
    private transient String passwordAdmin;
    private transient BlockingQueue<Notification> notifications;
    private boolean isActive;

    private MarketController marketController;
    private UserController userController;
    public Admin(int adminId, String email, String password){
        this.adminId = adminId;
        emailAdmin = email;
        passwordAdmin = password;
        isActive = false;
        notifications = new LinkedBlockingQueue<>();

    }

    public boolean getIsActive(){
        return isActive;
    }

    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public int getAdminId(){
        return adminId;
    }

    public String getEmailAdmin(){
        return emailAdmin;
    }
    public void closeStorePermanently(int storeId, int creatId) throws Exception {
        Set<Integer> userIds = marketController.closeStorePermanently(storeId);
        for(int userId : userIds){
            if(creatId != userId) {
                String notify = "the store: " + storeId + " has been permanently closed";
                Notification<String> notification = new Notification<>(NotificationOpcode.CLOSE_STORE_PERMANENTLY, notify);
                userController.addNotification(userId, notification);
                userController.removeStoreRole(userId, storeId);
            }
        }
    }
    public boolean checkEmail(String email){
        return email.equals(emailAdmin);
    }
    public boolean checkPassword(String pass){
        return Objects.equals(passwordAdmin, pass);
    }




    public void addControllers(UserController userController, MarketController marketController) {
        this.marketController = marketController;
        this.userController = userController;
    }

    public void cancelMembership(int userToRemove) throws Exception{
        List<Integer> storeIds = userController.cancelMembership(userToRemove);
        for(int storeId : storeIds)
            closeStorePermanently(storeId, userToRemove);
    }

    public synchronized void addNotification(Notification notification){
        notifications.offer(notification);
    }

    public List<Notification> displayNotifications(){
        List<Notification> display = new LinkedList<>();
        for (Notification notification : notifications)
            display.add(notification);
        notifications.clear();
        return display;
    }

    public Notification getNotification() throws InterruptedException {
        synchronized (notifications) {
            return notifications.take();
        }
    }
}
