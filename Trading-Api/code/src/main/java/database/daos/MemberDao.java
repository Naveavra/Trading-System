package database.daos;


import database.HibernateUtil;
import database.dtos.MemberDto;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MemberDao {

    public MemberDao() {

    }
    public void saveMember(MemberDto m){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.save(m);
            transaction.commit();
        }catch (Exception e){
            if(transaction != null)
                transaction.rollback();
        }
    }

    public void updateMember(MemberDto m){
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.saveOrUpdate(m);
            transaction.commit();
        }catch (Exception e){
            if(transaction != null)
                transaction.rollback();
        }
    }

    public MemberDto getMemberById(int id){
        Transaction transaction = null;
        MemberDto m = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            m = session.get(MemberDto.class, id);
            //transaction.commit();
        }catch (Exception e){
            if(transaction != null)
                transaction.rollback();
        }
        return m;
    }
}
