package utils.messageRelated;

import domain.user.Member;
import org.json.JSONObject;

public class ProductReview extends StoreReview{
    private int productId;

    public ProductReview(int messageId, NotificationOpcode opcode, String content, Member reviewer, int orderId, int storeId,
                         int productId, int rating){
        super(messageId, opcode, content, reviewer, orderId, storeId, rating);
        this.productId = productId;

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = toJsonHelp();
        json.put("productReviewId", getMessageId());
        json.put("orderId", orderId);
        json.put("storeId", storeId);
        json.put("productId", productId);
        json.put("rating", rating);
        return json;
    }


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
