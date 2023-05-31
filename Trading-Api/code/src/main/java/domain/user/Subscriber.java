package domain.user;

import utils.messageRelated.Notification;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Subscriber {
    protected transient BlockingQueue<Notification> notifications;

    public Subscriber(){
        notifications = new LinkedBlockingQueue<>();
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
