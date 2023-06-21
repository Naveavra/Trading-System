package utils.messageRelated;

import database.daos.MessageDao;
import domain.user.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.Session;
import org.json.JSONObject;


@Entity
@Table(name = "questions")
public class Question extends Message{

    private int storeId;
    private boolean gotFeedback;

    public Question(){
        this.opcode = NotificationOpcode.GET_STORE_DATA;
    }
    public Question(int messageId, String content, Member reviewer, int storeId){
        super(messageId, NotificationOpcode.GET_STORE_DATA, content, reviewer);
        this.storeId = storeId;
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
    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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
        json.put("questionId", getMessageId());
        json.put("storeId", storeId);
        json.put("gotFeedback", gotFeedback);
        return json;
    }

}
