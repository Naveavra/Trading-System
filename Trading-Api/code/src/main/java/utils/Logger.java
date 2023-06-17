package utils;
import database.Dao;
import database.DbEntity;

import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Logger{
    private static Logger instance;
    private List<Event> eventMap;
    
    private Logger() {
        eventMap = new LinkedList<>();
        List<? extends DbEntity> events = Dao.getAllInTable("Event");
        eventMap.addAll((List<Event>) events);
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