package utils;
import java.util.ArrayList;
import java.util.*;


public class Logger {
    private static Logger instance;
    private List<Event> eventMap;
    
    private Logger() {
        eventMap = new LinkedList<>();
    }
    
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(Event.LogStatus status, String content, String time, String userName) {
        Event event = new Event(status, content, time, userName);
        eventMap.add(event);
    }
    public List<Event> getEventMap(){
        return eventMap;
    }
}