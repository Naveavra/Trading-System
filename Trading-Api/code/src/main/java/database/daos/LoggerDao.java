package database.daos;

import database.HibernateUtil;
import database.dtos.LoggerDto;
import database.dtos.MemberDto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class LoggerDao {

    public LoggerDao() {

    }
    public void saveLog(LoggerDto l){
        DaoTemplate.save(l);
    }

    public void updateLog(LoggerDto l){
        DaoTemplate.update(l);
    }

    public List<LoggerDto> getLogs(){
        Transaction transaction = null;
        List<LoggerDto> list = new ArrayList<>();
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            String sql = "FROM LoggerDto";
            Query<LoggerDto> query = session.createQuery(sql, LoggerDto.class);
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
