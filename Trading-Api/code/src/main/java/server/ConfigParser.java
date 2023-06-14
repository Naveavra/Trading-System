package server;

import org.hibernate.cfg.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigParser {
    private Properties prop = new Properties();
    private FileInputStream input = null;
    private String configFilePath = "C:\\Users\\chais\\Documents\\BGU\\Year C\\S B\\WorkshopSE\\Git-Repo\\Trading-System\\config.properties";

    public ConfigParser() {
        // Load the config.properties file
        try {
            input = new FileInputStream(configFilePath);
            prop.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ConfigParser(String configFilePath) {
        this.configFilePath = configFilePath;

    }

    Properties getDBSettings() throws Exception {
        try {
            Properties settings = new Properties();
            settings.put(Environment.DRIVER, prop.getProperty("DB_DRIVER"));
            settings.put(Environment.URL, prop.getProperty("DB_URL"));
            settings.put(Environment.USER, prop.getProperty("DB_USER"));
            settings.put(Environment.PASS, prop.getProperty("DB_PASS"));
            settings.put(Environment.SHOW_SQL, prop.getProperty("DB_SHOW_SQL"));
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, prop.getProperty("DB_CURRENT_SESSION_CONTEXT_CLASS"));
            settings.put(Environment.HBM2DDL_AUTO, prop.getProperty("DB_HBM2DDL_AUTO"));
            return settings;
        }
        catch (Exception e)
        {
            throw new Exception("Miss config params to DB");
        }

    }

    void other()
    {
        String esPName = prop.getProperty("ES_P_NAME");
        String esPURL = prop.getProperty("ES_P_URL");
        String esPResponseTime = prop.getProperty("ES_P_RESPONSE_TIME");
        String esSName = prop.getProperty("ES_S_NAME");
        String esSURL = prop.getProperty("ES_S_URL");
        String esSResponseTime = prop.getProperty("ES_S_RESPONSE_TIME");
        String serverIP = prop.getProperty("Server_IP/Domain");
        String serverPort = prop.getProperty("Server_Port");
        String serverFIP = prop.getProperty("Server_F_IP/Domain");
        String serverFPort = prop.getProperty("Server_F_Port");
        String adminEmail = prop.getProperty("Admin_EMAIL");
        String adminPassword = prop.getProperty("Admin_PASSWORD");
    }

}
