package database.daos;

import database.HibernateUtil;
import database.dtos.AdminDto;
import database.dtos.MemberDto;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AdminDao {
    public AdminDao() {
    }

    public void saveAdmin(AdminDto a){
        DaoTemplate.save(a);
    }

    public void updateAdmin(AdminDto a){
        DaoTemplate.update(a);
    }
}
