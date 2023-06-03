package database.daos;

import database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DaoTemplate {

    public static void save(Object o){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.save(o);
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

    public static Object getById(Class c, int id){
        Transaction transaction = null;
        Object o = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            o = session.get(c, id);
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
        return o;
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
}
