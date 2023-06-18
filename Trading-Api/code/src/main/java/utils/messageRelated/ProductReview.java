package utils.messageRelated;

import database.daos.MessageDao;
import domain.store.product.Product;
import domain.user.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.json.JSONObject;

@Entity
@Table(name = "productReviews")
public class ProductReview extends StoreReview{
    private int productId;

    public ProductReview(){
        this.opcode = NotificationOpcode.GET_STORE_DATA;
    }
    public ProductReview(int messageId, String content, Member reviewer, int orderId, int storeId,
                         int productId, int rating){
        super(messageId, content, reviewer, orderId, storeId, rating);
        this.opcode = NotificationOpcode.GET_STORE_DATA;
        this.productId = productId;
        MessageDao.saveMessage(this);
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
