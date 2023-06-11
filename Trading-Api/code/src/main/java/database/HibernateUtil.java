package database;

import domain.states.*;
import domain.store.storeManagement.Store;
import domain.user.Member;
import domain.user.PurchaseHistory;
import market.Admin;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import utils.infoRelated.Receipt;

import java.util.Properties;


public class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            try{
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/sadnaDB");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "sadna11B");

                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);
                //TODO: add all tables needed to here
//                configuration.addAnnotatedClass(MemberDto.class);
//                configuration.addAnnotatedClass(AdminDto.class);
//                configuration.addAnnotatedClass(NotificationDto.class);
//                configuration.addAnnotatedClass(CartDto.class);
//                configuration.addAnnotatedClass(UserHistoryDto.class);
//                configuration.addAnnotatedClass(ReceiptDto.class);
//                configuration.addAnnotatedClass(StoreDto.class);
//                configuration.addAnnotatedClass(InventoryDto.class);
//
//                configuration.addAnnotatedClass(RoleDto.class);
//                configuration.addAnnotatedClass(PermissionDto.class);
//                configuration.addAnnotatedClass(AppointmentDto.class);
//                configuration.addAnnotatedClass(LoggerDto.class);
//
//                configuration.addAnnotatedClass(CategoryDto.class);
//                configuration.addAnnotatedClass(StoreReviewDto.class);
//                configuration.addAnnotatedClass(QuestionDto.class);
//                configuration.addAnnotatedClass(ProductReviewDto.class);
//                configuration.addAnnotatedClass(ConstraintDto.class);
//                configuration.addAnnotatedClass(DicountDto.class);
//
//                configuration.addAnnotatedClass(ComplaintDto.class);

                configuration.addAnnotatedClass(Member.class);
                configuration.addAnnotatedClass(Receipt.class);

                configuration.addAnnotatedClass(Admin.class);

                configuration.addAnnotatedClass(UserState.class);
                configuration.addAnnotatedClass(StoreManager.class);
                configuration.addAnnotatedClass(StoreCreator.class);
                configuration.addAnnotatedClass(StoreOwner.class);
                configuration.addAnnotatedClass(Permission.class);

                configuration.addAnnotatedClass(Store.class);





                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
