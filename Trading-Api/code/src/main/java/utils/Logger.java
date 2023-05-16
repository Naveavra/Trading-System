package utils;
import java.util.ArrayList;
import java.util.*;


public class Logger {
    public enum logStatus{ //maybe should be moved to a designated class
        Success,
        Fail,
    }
    private static Logger instance;
    private List<String> eventMap;
    private List<String> failMap;
    
    private Logger() {
        eventMap = new LinkedList<>();
        failMap = new LinkedList<>();
    }
    
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    /**
     * This function logs all activity in the system.
     * @param type Enum logStatus => Success, Fail, Error
     * @param message String
     */
    public void log(logStatus type,String message) {
        if(type == logStatus.Success)
            eventMap.add(message);
        else
            failMap.add(message);
    }
    public List<String> getEventMap(){
        return eventMap;
    }
    public List<String> getFailMap(){
        return failMap;
    }
}