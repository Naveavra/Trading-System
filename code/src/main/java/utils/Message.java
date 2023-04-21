package utils;

import domain.user.Member;

public class Message {
    private int messageId;
    private String content;
    private String ownerEmail;
    private int rating;
    private Member reviewer;
    private MessageState state;
    private int orderId; //if it is a review then orderId > -1 else orderId == -1
    private int storeId;
    private int productId;

    public Message(int messageId, String content, int rating, Member reviewer, int orderId, int storeId, MessageState ms){
        this.messageId = messageId;
        this.content = content;
        this.rating = rating;
        this.reviewer = reviewer;
        this.orderId = orderId;
        this.storeId = storeId;
        this.state = ms;
        productId = -1;
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

    public Member getReviewer() {
        return reviewer;
    }

    public String getContent() {
        return content;
    }

    public void addOrderId(int orderId){
        this.orderId = orderId;
    }

    public void sendFeedback(String feedback){
        reviewer.addNotification(feedback);
    }

    public MessageState getState(){
        return state;
    }

    //sending an email to the owner that a message was added to the store
    public void sendEmail(){

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
}
