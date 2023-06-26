package utils.messageRelated;

import database.daos.Dao;
import database.DbEntity;
import database.daos.SubscriberDao;
import domain.user.Member;
import jakarta.persistence.*;
import org.json.JSONObject;
import utils.infoRelated.Information;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Message extends Information implements DbEntity {
    @Id
    protected int messageId;
    @Transient
    protected NotificationOpcode opcode;

    protected String content;
    @Transient
    protected Member sender;
    protected int senderId;
    private boolean seen;


    public Message(){
    }
    public Message(int messageId, NotificationOpcode opcode, String content, Member reviewer){
        this.messageId = messageId;
        this.content = content;
        this.sender = reviewer;
        this.senderId = sender.getId();
        this.opcode  = opcode;
        seen = false;
    }

    public Member getSender() {
        return sender;
    }
    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public int getMessageId() {
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
        json.put("userId", senderId);
        json.put("seen", getSeen());
        return json;
    }

    @Override
    public void initialParams() throws Exception{
        getSenderFromDb();
    }
    protected void getSenderFromDb() throws Exception{
        if(sender == null)
            sender = SubscriberDao.getMember(senderId);
    }
}
