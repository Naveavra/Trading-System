package utils.messageRelated;


import database.DbEntity;
import database.daos.Dao;
import domain.user.Subscriber;
import jakarta.persistence.*;
import org.json.JSONObject;
import utils.infoRelated.Information;

@Entity
@Table(name = "notifications")
public class Notification extends Information implements DbEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int subId;
    private String notification;
    private NotificationOpcode opcode;


    public Notification(){
    }
    public Notification(NotificationOpcode opcode, String notification){
        this.opcode =  opcode;
        this.notification =  notification;
        Dao.save(this);
    }

    public String getNotification(){
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    @Override
    public void initialParams() {
    }
}
