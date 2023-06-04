package utils.messageRelated;

import domain.user.Member;
import org.json.JSONObject;
import utils.infoRelated.Information;

public abstract class Message extends Information {
    protected int messageId;
    protected NotificationOpcode opcode;
    protected String content;
    protected Member sender;
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
        json.put("userId", sender.getId());
        json.put("seen", getSeen());
        return json;
    }
}
