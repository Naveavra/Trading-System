package utils.messageRelated;

import domain.user.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.json.JSONObject;

import static utils.messageRelated.NotificationOpcode.PRODUCT_REVIEW;
import static utils.messageRelated.NotificationOpcode.QUESTION;

@Entity
@Table(name = "questions")
public class Question extends Message{

    private int storeId;
    private boolean gotFeedback;

    public Question(){
        this.opcode = QUESTION;
    }
    public Question(int messageId, String content, Member reviewer, int storeId){
        super(messageId, NotificationOpcode.QUESTION, content, reviewer);
        this.storeId = storeId;
        this.gotFeedback = false;
    }

    public void sendFeedback(String feedback) throws Exception{
        setSenderDb();
        if(!gotFeedback) {
            Notification notification = new Notification(NotificationOpcode.REVIEW_FEEDBACK, feedback);
            sender.addNotification(notification);
            gotFeedback = true;
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
