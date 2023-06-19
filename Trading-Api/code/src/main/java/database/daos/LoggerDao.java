package database.daos;

import database.DbEntity;
import utils.Event;

import java.util.ArrayList;
import java.util.List;

public class LoggerDao {
    private static  boolean log = false;

    public static void saveEvent(Event e){
        Dao.save(e);
    }

    public static List<Event> getLogs(){
        if(!log){
            List<? extends DbEntity> logs = Dao.getAllInTable("Event");
            log = true;
            return new ArrayList<>((List<Event>) logs);
        }
        return new ArrayList<>();
    }
}
