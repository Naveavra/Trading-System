package utils.messageRelated;

import database.daos.MessageDao;
import domain.user.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.Session;
import org.json.JSONObject;


@Entity
@Table(name = "complaints")
public class Complaint extends Message{

    private int orderId;
    private boolean gotFeedback;

    public Complaint(){
        this.opcode = NotificationOpcode.GET_ADMIN_DATA;
    }

    public Complaint(int messageId, String content, Member reviewer, int orderId){
        super(messageId, NotificationOpcode.GET_ADMIN_DATA, content, reviewer);
        this.orderId = orderId;
        this.gotFeedback = false;
    }

    public void sendFeedback(String feedback, Session session) throws Exception{
        if(!gotFeedback) {
            Notification notification = new Notification(NotificationOpcode.GET_STORE_DATA_AND_COMPLAINS, feedback);
            sender.addNotification(notification, session);
            gotFeedback = true;
            MessageDao.saveMessage(this, session);
        }
        else throw new Exception("the complaint already got an answer");

    }
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isGotFeedback() {
        return gotFeedback;
    }

    public void setGotFeedback(boolean gotFeedback) {
        this.gotFeedback = gotFeedback;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = toJsonHelp();
        json.put("complaintId", getMessageId());
        json.put("orderId", orderId);
        json.put("gotFeedback", gotFeedback);
        return json;
    }

}
