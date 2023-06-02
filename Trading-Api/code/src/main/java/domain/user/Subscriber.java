package domain.user;

import database.dtos.MemberDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import utils.infoRelated.LoginInformation;
import utils.messageRelated.Notification;
import utils.stateRelated.Action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Subscriber {

    protected int id;
    protected String email;
    protected String birthday;
    protected String password;
    protected boolean isConnected;
    protected MemberDto memberDto;
    protected BlockingQueue<Notification> notifications;
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

    public abstract LoginInformation getLoginInformation(String token);
    public abstract void checkPermission(Action action, int storeId)  throws Exception;

    //database
    public MemberDto getDto() {
        List<Notification> nlist = new ArrayList<>(notifications);
        memberDto.setNotifications(nlist);
        return memberDto;
    }

}
