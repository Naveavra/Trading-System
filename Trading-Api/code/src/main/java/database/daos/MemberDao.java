package database.daos;


import database.HibernateUtil;
import database.dtos.MemberDto;
import database.dtos.NotificationDto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.mockito.internal.matchers.Not;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

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
            String sql = String.format("FROM NotificationDto WHERE userId = %d", id);
            System.out.println(sql);
            Query<NotificationDto> query = session.createQuery(sql, NotificationDto.class);
            List<NotificationDto> nlist = query.list();
            for(NotificationDto n : nlist)
                System.out.println(n.getContent());
            //List<Object> n = query.getResultList();
            //System.out.println(n.get(0).toString());
            //NotificationDto n = (NotificationDto) session.get(s, NotificationDto.class);
            //System.out.println(n.getMemberDto());
            transaction.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
            if(transaction != null)
                transaction.rollback();
        }
        return m;
    }
}
