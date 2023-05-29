package utils;

import org.json.JSONObject;
import utils.infoRelated.Information;

public class Event extends Information {


    public static enum LogStatus{ //maybe should be moved to a designated class
        Success,
        Fail,
    }

    private int id;
    private LogStatus status;
    private String content;
    private String time;
    private String userName;

    public Event(int id, LogStatus status, String content, String time, String userName){
        this.id = id;
        this.status = status;
        this.content = content;
        this.time = time;
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
