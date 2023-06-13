package utils;

import jakarta.persistence.*;
import org.json.JSONObject;
import utils.infoRelated.Information;


@Entity
@Table(name = "logs")
public class Event extends Information {

    public enum LogStatus{ //maybe should be moved to a designated class
        Success,
        Fail,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private LogStatus status;
    private String content;
    private String time;
    private String userName;

    public Event(LogStatus status, String content, String time, String userName){
        this.status = status;
        this.content = content;
        this.time = time;
        this.userName = userName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LogStatus getStatus() {
        return status;
    }

    public void setStatus(LogStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public JSONObject toJson() {
       JSONObject json = new JSONObject();
       json.put("id", id);
       json.put("status", status.toString());
       json.put("content", content);
       json.put("time", time);
       json.put("userName", userName);
       return json;
    }
}
