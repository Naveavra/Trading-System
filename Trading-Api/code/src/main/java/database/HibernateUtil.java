package database;

import database.dtos.*;
import domain.states.*;
import domain.store.product.Product;
import domain.store.storeManagement.Bid;
import domain.store.storeManagement.Store;
import domain.user.Member;
import market.Admin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import server.Config.ConfigParser;
import utils.Event;
import utils.infoRelated.Receipt;
import utils.messageRelated.*;

import java.util.Properties;
import java.util.logging.Level;


public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static Properties settings = ConfigParser.getInstance().getDBSetting();
    public static boolean createDrop = false;
    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            try{
                Configuration configuration = new Configuration();
                if(createDrop)
                    settings.put(Environment.HBM2DDL_AUTO, "create-drop");
                java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
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
                configuration.addAnnotatedClass(Appointment.class);

                configuration.addAnnotatedClass(Store.class);
                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(CategoryDto.class);
                configuration.addAnnotatedClass(ConstraintDto.class);
                configuration.addAnnotatedClass(DiscountDto.class);

                configuration.addAnnotatedClass(Bid.class);
                configuration.addAnnotatedClass(ApproverDto.class);

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
