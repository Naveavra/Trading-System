package utils.messageRelated;


public class Notification<T>{
    private T notification;


    public Notification(T notification){
        this.notification =  notification;
    }

    public T getNotification(){
        return notification;
    }

    public String toString() {
        return "the value of the notification is: " + notification.toString();
    }



}
