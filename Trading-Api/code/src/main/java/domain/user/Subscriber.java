package domain.user;

import database.Dao;
import database.DbEntity;
import jakarta.persistence.*;
import utils.infoRelated.LoginInformation;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;




@Entity
@Table(name = "users")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Subscriber implements DbEntity{

    @Id
    @SequenceGenerator(name = "ids", sequenceName = "ids", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ids")
    protected int id;
    protected String email;
    protected String birthday;
    protected String password;

    @Transient
    protected boolean isConnected;

    @Transient
    protected BlockingQueue<Notification> notifications;

    public Subscriber(){
    }
    public Subscriber(String email, String password){
        this.email = email;
        this.password = password;
        this.birthday = "no input";
        notifications = new LinkedBlockingQueue<>();
        isConnected = false;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return email;
    }
    public String getPassword(){return password;}

    public String getBirthday(){return birthday;}

    public void connect(){
        isConnected = true;
    }

    public void disconnect(){
        isConnected = false;
    }
    public boolean getIsConnected(){
        return isConnected;
    }

    public void login(String password) throws Exception{
        if(isConnected)
            throw new Exception("the member is already connected");
        if (this.password.equals(password)) {
            connect();
            return;
        }
        throw new Exception("wrong password");
    }

    public synchronized void addNotification(Notification notification){
        notification.setSubId(id);
        boolean got = notifications.offer(notification);
        if(got)
            Dao.save(notification);
    }

    public List<Notification> displayNotifications(){
        List<Notification> display = new LinkedList<>(notifications);
        Dao.removeIf(Notification.class,"Notification", String.format("subId = %d", id));
        notifications.clear();
        return display;
    }

    public Notification getNotification() throws InterruptedException {
        synchronized (notifications) {
            Notification n = notifications.take();
            if(isConnected) {
                Dao.remove(n);
                return n;
            }else{
                notifications.offer(n);
                return null;
            }
        }
    }

    public void initialNotificationsFromDb(){
        if(notifications == null) {
            notifications = new LinkedBlockingQueue<>();
            List<? extends DbEntity> notifics = Dao.getListById(Notification.class, id, "Notification", "subId");
            for (Notification n : (List<Notification>) notifics)
                addNotification(n);
        }
    }

    public abstract LoginInformation getLoginInformation(String token);
    public abstract void checkPermission(Action action, int storeId)  throws Exception;
}
