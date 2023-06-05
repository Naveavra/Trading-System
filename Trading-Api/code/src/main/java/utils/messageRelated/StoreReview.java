package utils.messageRelated;

import domain.user.Member;
import org.json.JSONObject;

public class StoreReview extends Message{

    protected int orderId;
    protected int storeId;
    protected int rating;

    public StoreReview(){

    }

    public StoreReview(int messageId, NotificationOpcode opcode, String content, Member reviewer, int orderId, int storeId,
                       int rating){
        super(messageId, opcode, content, reviewer);
        this.orderId = orderId;
        this.storeId = storeId;
        this.rating = rating;
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
