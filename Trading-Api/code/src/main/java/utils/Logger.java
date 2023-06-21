package utils;
import database.daos.Dao;
import database.DbEntity;
import database.daos.LoggerDao;

import java.util.*;


public class Logger{
    private static Logger instance;
    private List<Event> eventMap;
    
    private Logger(){
        eventMap = new LinkedList<>();
        try {
            List<? extends DbEntity> events = LoggerDao.getLogs();
            eventMap.addAll((List<Event>) events);
        }catch (Exception ignored){}
    }
    
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(Event e) {
        eventMap.add(e);
    }
    public List<Event> getEventMap(){
        return eventMap;
    }
}