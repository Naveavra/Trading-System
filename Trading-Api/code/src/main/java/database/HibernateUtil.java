package database;

import database.dtos.AppointmentDto;
import database.dtos.CartDto;
import database.dtos.CategoryDto;
import database.dtos.ReceiptDto;
import domain.states.*;
import domain.store.product.Product;
import domain.store.storeManagement.Store;
import domain.user.Member;
import market.Admin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import utils.Event;
import utils.infoRelated.Receipt;
import utils.messageRelated.*;

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
                //settings.put(Environment.HBM2DDL_AUTO, "create-drop");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);
                //TODO: add all tables needed to here
                configuration.addAnnotatedClass(Member.class);
                configuration.addAnnotatedClass(Notification.class);
                configuration.addAnnotatedClass(CartDto.class);
                configuration.addAnnotatedClass(Receipt.class);
                configuration.addAnnotatedClass(ReceiptDto.class);

                configuration.addAnnotatedClass(Admin.class);

                configuration.addAnnotatedClass(Complaint.class);
                configuration.addAnnotatedClass(StoreReview.class);
                configuration.addAnnotatedClass(ProductReview.class);
                configuration.addAnnotatedClass(Question.class);

                configuration.addAnnotatedClass(UserState.class);
                configuration.addAnnotatedClass(StoreManager.class);
                configuration.addAnnotatedClass(StoreCreator.class);
                configuration.addAnnotatedClass(StoreOwner.class);
                configuration.addAnnotatedClass(Permission.class);
                configuration.addAnnotatedClass(AppointmentDto.class);

                configuration.addAnnotatedClass(Store.class);
                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(CategoryDto.class);

                configuration.addAnnotatedClass(Event.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static boolean checkDbConnected(){
        if(sessionFactory == null)
            return false;
        boolean isConnected = false;
        Session session = sessionFactory.openSession();
        isConnected = session.isConnected();
        session.close();
        return isConnected;

    }
}
