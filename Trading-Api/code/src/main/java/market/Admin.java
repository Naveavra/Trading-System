package market;

import database.daos.Dao;
import domain.user.Subscriber;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.Session;
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
    public Admin(int id, String email, String password){
        super(id, email, password);
        Pair<UserController, MarketController> controllers = Market.getControllers();
        userController = controllers.getFirst();
        marketController = controllers.getSecond();
    }

    public void closeStorePermanently(int storeId, int creatId, Session session) throws Exception {
        Set<Integer> userIds = marketController.closeStorePermanently(storeId, session);
        for(int userId : userIds){
            if(creatId != userId) {
                String notify = "the store: " + storeId + " has been permanently closed";
                Notification notification = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA, notify);
                userController.addNotification(userId, notification, session);
                userController.removeStoreRole(userId, storeId, session);
            }
        }
    }
    public boolean checkEmail(String email){
        return email.equals(this.email);
    }
    public boolean checkPassword(String pass){
        return pass.equals(this.password);
    }

    public void cancelMembership(int userToRemove, Session session) throws Exception{
        List<Integer> storeIds = userController.cancelMembership(userToRemove, session);
        for(int storeId : storeIds)
            closeStorePermanently(storeId, userToRemove, session);
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

    public void saveAdmin(Session session) throws Exception {
        Dao.save(this, session);
    }

    @Override
    public void initialParams() throws Exception {
        initialNotificationsFromDb();
    }
}
