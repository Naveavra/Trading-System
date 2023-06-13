package domain.user;

import database.daos.DaoTemplate;
import database.dtos.MemberDto;
import jakarta.persistence.*;
import utils.infoRelated.LoginInformation;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;




@Entity
@Table(name = "users")
public abstract class Subscriber {

    @Id
    protected int id;
    protected String email;
    protected String birthday;
    protected String password;

    @Transient
    protected boolean isConnected;
    @Transient
    protected MemberDto memberDto;

    @Transient
    protected BlockingQueue<Notification> notifications;

    public Subscriber(){
    }
    public Subscriber(int id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
        this.birthday = "no input";
        notifications = new LinkedBlockingQueue<>();
        isConnected = false;
        memberDto = new MemberDto(id, email, password, birthday);
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
        if(got) {
            DaoTemplate.save(notification);
        }

    }

    public List<Notification> displayNotifications(){
        List<Notification> display = new LinkedList<>(notifications);
        DaoTemplate.removeIf("Notification", String.format("subId = %d", id));
        notifications.clear();
        return display;
    }

    public Notification getNotification() throws InterruptedException {
        synchronized (notifications) {
            Notification n = notifications.take();
            DaoTemplate.remove(n);
            return n;
        }
    }

    public abstract LoginInformation getLoginInformation(String token);
    public abstract void checkPermission(Action action, int storeId)  throws Exception;

    //database
    public MemberDto getDto() {
        List<Notification> nlist = new ArrayList<>(notifications);
        memberDto.setNotifications(nlist);
        return memberDto;
    }

}
