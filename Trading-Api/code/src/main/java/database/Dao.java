package database;

import database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dao {

    private static HashMap<Class, HashMap<Integer, Object>> identityMap;

    public static void save(Object o){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.saveOrUpdate(o);
            transaction.commit();
        }catch (Exception e){
            if(transaction != null)
                transaction.rollback();
        }
    }

    public static void update(Object o){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.saveOrUpdate(o);
            transaction.commit();
        }catch (Exception e){
            if(transaction != null)
                transaction.rollback();
        }
    }

    public static void remove(Object o) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.remove(o);
            transaction.commit();
        }catch (Exception e){
            if(transaction != null)
                transaction.rollback();
        }
    }

    public static Object getById(Class c, int id){
        Object o = getObjectIfLoaded(c, id);
        if(o != null)
            return o;
        Transaction transaction = null;
        o = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            o = session.get(c, id);
            putInMap(c, id, o);
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
        return o;
    }

    public static Object getByParam(Class c, String entityName, String param){
        Transaction transaction = null;
        Object o = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = String.format("FROM %s WHERE %s", entityName, param);
            Query query = session.createQuery(sql, c);
            o = query.uniqueResult();
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
        return o;
    }

    public static List<?> getAllInTable(String entityName){
        Transaction transaction = null;
        List<?> list = new ArrayList<>();
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = String.format("FROM %s", entityName);
            Query query = session.createQuery(sql);
            list = query.list();
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
        return list;
    }

    public static List<?> getListById(Class c, int id, String entityName, String param){
        Transaction transaction = null;
        List<?> list = new ArrayList<>();
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = String.format("FROM %s WHERE %s = %d", entityName, param, id);
            Query query = session.createQuery(sql, c);
            list = query.list();
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
        return list;
    }

    public static List<?> getListByCompositeKey(Class c, int id1, int id2, String entityName, String param1, String param2){
        Transaction transaction = null;
        List<?> list = new ArrayList<>();
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = String.format("FROM %s WHERE %s = %d and %s = %d", entityName, param1, id1, param2, id2);
            Query query = session.createQuery(sql, c);
            list = query.list();
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
        return list;
    }

    public static void removeIf(String entityName, String param) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = String.format("DELETE FROM %s WHERE %s", entityName, param);
            Query q = session.createQuery(sql);
            q.executeUpdate();
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
//        String query = "DELETE FROM table_name WHERE condition";
    }

    public static void updateFor(String entityName, String sets, String conditions) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = String.format("UPDATE %s SET %s WHERE %s", entityName, sets, conditions);
            Query q = session.createQuery(sql);
            q.executeUpdate();
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
    }

    private static Object getObjectIfLoaded(Class c, int id){
        if(identityMap == null)
            identityMap = new HashMap<>();
        if(identityMap.containsKey(c))
            if(identityMap.get(c).containsKey(id))
                return identityMap.get(c).get(id);
        return null;
    }

    private static void putInMap(Class c, int id, Object o) {
        if(o != null){
            if(!identityMap.containsKey(c))
                identityMap.put(c, new HashMap<>());
            identityMap.get(c).put(id, o);
        }
    }
}
