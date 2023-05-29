package utils;
import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Logger {
    private static Logger instance;
    private AtomicInteger loggerId;
    private List<Event> eventMap;
    
    private Logger() {
        eventMap = new LinkedList<>();
        loggerId = new AtomicInteger(1);
    }
    
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(Event.LogStatus status, String content, String time, String userName) {
        Event event = new Event(loggerId.getAndIncrement(), status, content, time, userName);
        eventMap.add(event);
    }
    public List<Event> getEventMap(){
        return eventMap;
    }
}