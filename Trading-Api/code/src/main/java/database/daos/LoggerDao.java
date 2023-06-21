package database.daos;

import database.DbEntity;
import database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.Event;

import java.util.ArrayList;
import java.util.List;

public class LoggerDao {
    private static  boolean log = false;

    public static void saveEvent(Event e){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Dao.save(e, session);
        }catch (Exception ignored){
        }
    }

    public static List<Event> getLogs() throws Exception{
        if(!log){
            List<? extends DbEntity> logs = Dao.getAllInTable("Event");
            log = true;
            return new ArrayList<>((List<Event>) logs);
        }
        return new ArrayList<>();
    }
}
