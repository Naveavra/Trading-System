package database.daos;

import database.DbEntity;
import database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dao {
    private static boolean forTests = false;

    public static void setForTests(boolean forTestsTmp){
        forTests = forTestsTmp;
    }

    public static void save(DbEntity o, Session session) throws Exception{
        if(!forTests) {
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                session.saveOrUpdate(o);
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("saving the item in db failed");
            }
        }
    }

    public static void remove(DbEntity o, Session session) throws Exception{
        if(!forTests) {
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                session.remove(o);
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("removing the item from db failed");
            }
        }
    }

    public static DbEntity getById(Class c, int id) throws Exception{
        if(!forTests) {
            DbEntity o = null;
            Transaction transaction = null;
            try(Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                o = (DbEntity) session.get(c, id);
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("getting item from db failed");
            }
            return o;
        }
        return null;
    }

    public static DbEntity getByParam(Class c, String entityName, String param) throws Exception{
        if(!forTests) {
            DbEntity o = null;
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                String sql = String.format("FROM %s WHERE %s", entityName, param);
                Query<DbEntity> query = session.createQuery(sql, c);
                o = query.uniqueResult();
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("getting item from db failed");
            }
            return o;
        }
        return null;
    }

    public static List<DbEntity> getByParamList(Class c, String entityName, String param) throws Exception{
        if(!forTests) {
            List<DbEntity> list = new ArrayList<>();
            Transaction transaction = null;
            try(Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                String sql = String.format("FROM %s WHERE %s", entityName, param);
                Query<DbEntity> query = session.createQuery(sql, c);
                list = query.list();
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("getting list from db failed");
            }
            return list;
        }
        return new ArrayList<>();
    }

    public static List<DbEntity> getAllInTable(String entityName) throws Exception{
        if(!forTests) {
            List<DbEntity> list = new ArrayList<>();
            Transaction transaction = null;
            try(Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                String sql = String.format("FROM %s", entityName);
                Query<DbEntity> query = session.createQuery(sql);
                list = query.list();
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("getting list from db failed");
            }
            return list;
        }
        return new ArrayList<>();
    }

    public static List<DbEntity> getListById(Class c, int id, String entityName, String param) throws Exception{
        if(!forTests) {
            List<DbEntity> list = new ArrayList<>();
            Transaction transaction = null;
            try(Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                String sql = String.format("FROM %s WHERE %s = %d", entityName, param, id);
                Query<DbEntity> query = session.createQuery(sql, c);
                list = query.list();
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("getting list from db failed");
            }
            return list;
        }
        return new ArrayList<>();
    }

    public static List<DbEntity> getListByCompositeKey(Class c, int id1, int id2, String entityName, String param1, String param2) throws Exception{
        if(!forTests) {
            List<DbEntity> list = new ArrayList<>();
            Transaction transaction = null;
            try(Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                String sql = String.format("FROM %s WHERE %s = %d and %s = %d", entityName, param1, id1, param2, id2);
                Query<DbEntity> query = session.createQuery(sql, c);
                list = query.list();
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("getting list from db failed");
            }
            return list;
        }
        return new ArrayList<>();
    }

    public static void removeIf(String entityName, String param, Session session) throws Exception {
        if(!forTests) {
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                String sql = String.format("DELETE FROM %s WHERE %s", entityName, param);
                Query q = session.createQuery(sql);
                q.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("removing from db failed");
            }
        }
    }

    public static void updateFor(String entityName, String sets, String conditions, Session session) throws Exception{
        if(!forTests) {
            Transaction transaction = null;
            try{
                transaction = session.beginTransaction();
                String sql = String.format("UPDATE %s SET %s WHERE %s", entityName, sets, conditions);
                Query q = session.createQuery(sql);
                q.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if(transaction != null)
                    transaction.rollback();
                throw new Exception("updating rows in db failed");
            }
        }
    }

    public static boolean getForTests(){
        return forTests;
    }
}
