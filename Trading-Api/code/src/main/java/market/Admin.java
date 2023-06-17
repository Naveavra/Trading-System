package market;

import database.Dao;
import domain.user.Subscriber;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import service.MarketController;
import service.UserController;
import utils.Pair;
import utils.infoRelated.LoginInformation;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;
import utils.stateRelated.Action;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "admins")
public class Admin extends Subscriber {
    @Transient
    private MarketController marketController;
    @Transient
    private UserController userController;

    public Admin(){
        setControllers();
    }
    public Admin(String email, String password){
        super(email, password);
        Pair<UserController, MarketController> controllers = Market.getControllers();
        userController = controllers.getFirst();
        marketController = controllers.getSecond();
    }

    public void closeStorePermanently(int storeId, int creatId) throws Exception {
        Set<Integer> userIds = marketController.closeStorePermanently(storeId);
        for(int userId : userIds){
            if(creatId != userId) {
                String notify = "the store: " + storeId + " has been permanently closed";
                Notification notification = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA, notify);
                userController.addNotification(userId, notification);
                userController.removeStoreRole(userId, storeId);
            }
        }
    }
    public boolean checkEmail(String email){
        return email.equals(this.email);
    }
    public boolean checkPassword(String pass){
        return pass.equals(this.password);
    }

    public void cancelMembership(int userToRemove) throws Exception{
        List<Integer> storeIds = userController.cancelMembership(userToRemove);
        for(int storeId : storeIds)
            closeStorePermanently(storeId, userToRemove);
    }

    public void setControllers(){
        Pair<UserController, MarketController> controllers = Market.getControllers();
        userController = controllers.getFirst();
        marketController = controllers.getSecond();
    }


    @Override
    public LoginInformation getLoginInformation(String token) {
        return new LoginInformation(token, getId(), getName(), true, displayNotifications(),
                null, null, null, null, null, -1, null, null);
    }

    @Override
    public void checkPermission(Action action, int storeId) throws Exception {
    }

    public void saveAdmin() {
        Dao.save(this);
    }

    @Override
    public void initialParams() {
        initialNotificationsFromDb();
    }
}
