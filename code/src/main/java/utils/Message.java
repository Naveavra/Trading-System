package utils;

import domain.user.Member;

public class Message {
    private String content;
    private String ownerEmail;
    private int rating;
    private Member reviewer;
    private MessageState state;
    private int orderId; //if it is a review then orderId > -1 else orderId == -1

    public Message(String content, int rating, Member reviewer){
        this.content = content;
        this.rating = rating;
        this.reviewer = reviewer;
        orderId = -1;
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
}
