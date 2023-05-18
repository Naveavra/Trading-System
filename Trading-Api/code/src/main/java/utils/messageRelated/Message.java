package utils.messageRelated;

import domain.user.Member;
import org.json.JSONObject;
import utils.infoRelated.Information;

public class Message extends Information {
    private transient int messageId;
    private String content;
    private String ownerEmail;
    private int rating;
    private transient Member reviewer;
    private MessageState state;
    private int orderId; //if it is a review then orderId > -1 else orderId == -1
    private int storeId;
    private int productId;
    private boolean gotFeedback;
    private boolean seen;

    public Message(int messageId, String content, Member reviewer, int orderId, int storeId, MessageState ms){
        this.messageId = messageId;
        this.content = content;
        this.rating = -1;
        this.reviewer = reviewer;
        this.orderId = orderId;
        this.storeId = storeId;
        this.state = ms;
        productId = -1;
        ownerEmail = null;
        gotFeedback = false;
        seen = false;
    }

    public void addRating(int rating){
        this.rating = rating;
    }
    public int getRating() {
        return rating;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getProductId(){return productId;}
    public MessageState getMessageState() {
        return state;
    }

    public Member getReviewer() {
        return reviewer;
    }

    public String getContent() {
        return content;
    }

    public void addOrderId(int orderId){
        this.orderId = orderId;
    }

    public void sendFeedback(String feedback) throws Exception{
        if(state == MessageState.question){
            if(!gotFeedback) {
                Notification<String> notification = new Notification<>(feedback);
                reviewer.addNotification(notification);
                sendEmail();
                gotFeedback = true; //NAVE
            }
        }
        else
            throw new Exception("the message already got an answer");
    }

    public MessageState getState(){
        return state;
    }

    //TODO:needs to check how to email the reviewer and the owner(using owner email)
    private void sendEmail(){
        if(ownerEmail != null){

        }
    }
    public void addOwnerEmail(String email){
        ownerEmail = email;
    }


    public Integer getMessageId() {
        return messageId;
    }

    public void addProductToReview(int productId) throws Exception{
        if(state == MessageState.reviewProduct)
            this.productId = productId;
        else
            throw new Exception("the message isn't a review for a product");
    }

    public boolean gotFeedback(){return gotFeedback;}

    public void markAsRead(){
        seen = true;
    }

    public boolean getSeen(){
        return seen;
    }

    @Override
    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("messageId", getMessageId());
        json.put("content", getContent());
        json.put("rating", getRating());
        json.put("state", getMessageState());
        json.put("orderId", getOrderId());
        json.put("storeId", getStoreId());
        json.put("productId", getProductId());
        json.put("gotFeedback", gotFeedback());
        json.put("seen", getSeen());
        return json;
    }
}
