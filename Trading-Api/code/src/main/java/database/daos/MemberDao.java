package database.daos;


import database.HibernateUtil;
import database.dtos.MemberDto;
import database.dtos.NotificationDto;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MemberDao {

    public MemberDao() {

    }
    public void saveMember(MemberDto m){
        DaoTemplate.save(m);
    }

    public void updateMember(MemberDto m){
        DaoTemplate.update(m);
    }

    public MemberDto getMemberById(int id){
        Transaction transaction = null;
        MemberDto m = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            m = session.get(MemberDto.class, id);
            transaction.commit();
        }catch (Exception e){
            if(transaction != null)
                transaction.rollback();
        }
        return m;
    }
}
