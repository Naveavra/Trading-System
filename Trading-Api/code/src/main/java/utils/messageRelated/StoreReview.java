package utils.messageRelated;

import database.daos.MessageDao;
import domain.user.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import org.json.JSONObject;


@Entity
@Table(name = "storeReviews")
public class StoreReview extends Message{

    protected int orderId;
    protected int storeId;
    protected int rating;

    public StoreReview(){
        this.opcode = NotificationOpcode.GET_STORE_DATA;
    }

    public StoreReview(String content, Member reviewer, int orderId, int storeId,
                       int rating){
        super(NotificationOpcode.GET_STORE_DATA, content, reviewer);
        this.orderId = orderId;
        this.storeId = storeId;
        this.rating = rating;
        MessageDao.saveMessage(this);
    }
    @Override
    public JSONObject toJson() {
        JSONObject json = toJsonHelp();
        json.put("storeReviewId", getMessageId());
        json.put("orderId", orderId);
        json.put("storeId", storeId);
        json.put("rating", rating);
        return json;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
