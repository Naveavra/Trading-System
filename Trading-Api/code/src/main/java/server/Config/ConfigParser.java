package server.Config;

import market.Admin;
import org.hibernate.cfg.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigParser {
    private Properties prop = new Properties();
    private Properties DBSetting;
    private Admin initialAdmin;
    private final int ADMIN_START_ID = 1;
    private FileInputStream input = null;
    private String configFilePath = "C:\\Users\\chais\\Documents\\BGU\\Year C\\S B\\WorkshopSE\\Git-Repo\\Trading-System\\config.properties";

    public ConfigParser() {
        try {
            // Load the config.properties file
            input = new FileInputStream(configFilePath);
            prop.load(input);
            initSettings();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ConfigParser(String configFilePath) {
        this.configFilePath = configFilePath;
        try {
            // Load the config.properties file
            input = new FileInputStream(configFilePath);
            prop.load(input);
            initSettings();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initSettings()
    {
        initDBSettings();
        initESPaymentSettings();
        initESSupplySettings();
        initAdminSettings();
    }

    private void  initDBSettings() {
        DBSetting = null;
        String dbDriver = prop.getProperty("DB_DRIVER");
        String dbURL = prop.getProperty("DB_URL");
        String dbUser = prop.getProperty("DB_USER");
        String dbPassword = prop.getProperty("DB_PASS");
        String showSQL = prop.getProperty("DB_SHOW_SQL");
        String sessionContextClass = prop.getProperty("DB_CURRENT_SESSION_CONTEXT_CLASS");
        String hbm2ddlAuto = prop.getProperty("DB_HBM2DDL_AUTO");
        if (dbDriver != null)
        {
            DBSetting = new Properties();
            DBSetting.put(Environment.DRIVER, prop.getProperty("DB_DRIVER"));
            DBSetting.put(Environment.URL, prop.getProperty("DB_URL"));
            DBSetting.put(Environment.USER, prop.getProperty("DB_USER"));
            DBSetting.put(Environment.PASS, prop.getProperty("DB_PASS"));
            DBSetting.put(Environment.SHOW_SQL, prop.getProperty("DB_SHOW_SQL"));
            DBSetting.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, prop.getProperty("DB_CURRENT_SESSION_CONTEXT_CLASS"));
            DBSetting.put(Environment.HBM2DDL_AUTO, prop.getProperty("DB_HBM2DDL_AUTO"));
        }
    }


    void initESPaymentSettings()
    {
        String esPName = prop.getProperty("ES_P_NAME");
        String esPURL = prop.getProperty("ES_P_URL");
        String esPResponseTime = prop.getProperty("ES_P_RESPONSE_TIME");
    }

    private void initAdminSettings() {
        String adminEmail = prop.getProperty("Admin_EMAIL");
        String adminPassword = prop.getProperty("Admin_PASSWORD");
        initialAdmin = new Admin(ADMIN_START_ID, adminEmail, adminPassword);
    }

    private void extracted() {
        //TODO: Wait for answer from eli
        String serverIP = prop.getProperty("Server_IP/Domain");
        String serverPort = prop.getProperty("Server_Port");

        String serverFIP = prop.getProperty("Server_F_IP/Domain");
        String serverFPort = prop.getProperty("Server_F_Port");
    }

    private void initESSupplySettings() {
        String esSName = prop.getProperty("ES_S_NAME");
        String esSURL = prop.getProperty("ES_S_URL");
        String esSResponseTime = prop.getProperty("ES_S_RESPONSE_TIME");
        if (esSName != null && esSURL != null && esSResponseTime != null) {

        }
    }

}
