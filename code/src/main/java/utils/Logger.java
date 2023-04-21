package utils;
import java.util.ArrayList;
import java.util.*;


public class Logger {
    public enum logStatus{ //maybe should be moved to a designated class
        Success,
        Fail,
    }
    private static Logger instance;
    private HashMap<logStatus,List<String>> logMap;
    
    private Logger() {
        logMap.put(logStatus.Success, new ArrayList<>());
        logMap.put(logStatus.Fail, new ArrayList<>());
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
        logMap.get(type).add(message);
    }
}