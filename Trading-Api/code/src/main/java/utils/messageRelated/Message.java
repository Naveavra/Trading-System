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
    @GeneratedValue(strategy = GenerationType.TABLE)
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
    public Message(NotificationOpcode opcode, String content, Member reviewer){
        this.content = content;
        this.sender = reviewer;
        this.senderId = sender.getId();
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

    @Override
    public void initialParams(){
        getSenderFromDb();
    }
    protected void getSenderFromDb(){
        if(sender == null)
            sender = SubscriberDao.getMember(senderId);
    }

}
