package utils.messageRelated;


import org.json.JSONObject;
import utils.infoRelated.Information;

public class Notification<T> extends Information {
    private T notification;
    private NotificationOpcode opcode;


    public Notification(NotificationOpcode opcode, T notification){
        this.opcode =  opcode;
        this.notification =  notification;
    }

    public T getNotification(){
        return notification;
    }

    public String toString() {
        return "the value of the notification is: " + notification.toString();
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("opcode", opcode.ordinal());
        json.put("content", notification);
        return json;
    }

    public NotificationOpcode getOpcode() {
        return opcode;
    }

    public void setOpcode(NotificationOpcode opcode) {
        this.opcode = opcode;
    }
}
