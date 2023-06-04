package utils.messageRelated;

import domain.user.Member;
import org.json.JSONObject;
import utils.infoRelated.Information;

public abstract class Message extends Information {
    protected int messageId;
    protected NotificationOpcode opcode;
    protected String content;
    //private int rating;
    protected Member sender;
    //private int orderId; //if it is a review then orderId > -1 else orderId == -1
    //private int storeId;
    //private int productId;
    //private boolean gotFeedback;
    private boolean seen;


    public Message(){
    }
    public Message(int messageId, NotificationOpcode opcode, String content, Member reviewer){
        this.messageId = messageId;
        this.content = content;
        this.sender = reviewer;
        this.opcode  = opcode;
        seen = false;
    }

    public Member getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void markAsRead(){
        seen = true;
    }

    public boolean getSeen(){
        return seen;
    }

    @Override
    public abstract JSONObject toJson();

    protected JSONObject toJsonHelp(){
        JSONObject json = new JSONObject();
        json.put("messageId", getMessageId());
        json.put("opcode", opcode.ordinal());
        json.put("content", getContent());
        json.put("seen", getSeen());
        return json;
    }
//    {
//        JSONObject json = new JSONObject();
//        json.put("messageId", getMessageId());
//        json.put("opcode", opcode.ordinal());
//        json.put("content", getContent());
//        json.put("rating", getRating());
//        json.put("state", getMessageState());
//        json.put("orderId", getOrderId());
//        json.put("storeId", getStoreId());
//        json.put("productId", getProductId());
//        json.put("gotFeedback", gotFeedback());
//        json.put("seen", getSeen());
//        return json;
//    }
}
