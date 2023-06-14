package server.Config;

import market.Admin;
import org.hibernate.cfg.Environment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * The ConfigParser class is responsible for parsing the config.properties file and initializing various settings.
 */
public class ConfigParser {
    private final Properties prop = new Properties();
    private Properties DBSetting;
    private ESConfig supplyConfig;
    private ESConfig paymentConfig;
    private Admin initialAdmin;
    private final int ADMIN_START_ID = 1;
    private FileInputStream input = null;
    private final String configFilePath;

    /**
     * Constructs a ConfigParser instance with the default config file path.
     */
    public ConfigParser() {
        this("config.properties");
    }

    /**
     * Constructs a ConfigParser instance with a custom config file path.
     *
     * @param configFilePath The path to the config.properties file.
     */
    public ConfigParser(String configFilePath) {
        this.configFilePath = configFilePath;
        loadProperties();
        initSettings();
    }

    /**
     * Loads the properties from the config file.
     */
    private void loadProperties() {
        try {
            input = new FileInputStream(configFilePath);
            prop.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file not found: " + configFilePath, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file: " + configFilePath, e);
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

    /**
     * Initializes all the settings from the loaded properties.
     */
    private void initSettings() {
        initDBSettings();
        initESConfigSettings("ES_S", supplyConfig);
        initESConfigSettings("ES_P", paymentConfig);
        initAdminSettings();
    }

    /**
     * Initializes the database settings.
     */
    private void initDBSettings() {
        String dbDriver = prop.getProperty("DB_DRIVER");
        String dbURL = prop.getProperty("DB_URL");
        String dbUser = prop.getProperty("DB_USER");
        String dbPassword = prop.getProperty("DB_PASS");
        String showSQL = prop.getProperty("DB_SHOW_SQL");
        String sessionContextClass = prop.getProperty("DB_CURRENT_SESSION_CONTEXT_CLASS");
        String hbm2ddlAuto = prop.getProperty("DB_HBM2DDL_AUTO");

        if (dbDriver != null && dbURL != null && dbUser != null && dbPassword != null) {
            DBSetting = new Properties();
            DBSetting.put(Environment.DRIVER, dbDriver);
            DBSetting.put(Environment.URL, dbURL);
            DBSetting.put(Environment.USER, dbUser);
            DBSetting.put(Environment.PASS, dbPassword);
            DBSetting.put(Environment.SHOW_SQL, showSQL);
            DBSetting.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, sessionContextClass);
            DBSetting.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        } else {
            throw new IllegalArgumentException("Missing or incomplete database properties");
        }
    }

    /**
     * Initializes the external service configuration settings.
     *
     * @param prefix The prefix for the properties (e.g., "ES_S" or "ES_P").
     * @param config The ESConfig object to initialize.
     */
    private void initESConfigSettings(String prefix, ESConfig config) {
        String name = prop.getProperty(prefix + "_NAME");
        String url = prop.getProperty(prefix + "_URL");
        String responseTime = prop.getProperty(prefix + "_RESPONSE_TIME");

        if (name != null && url != null) {
            if (responseTime != null) {
                try {
                    int time = Integer.parseInt(responseTime);
                    if (time >= 0) {
                        config = new ESConfig(name, url, time);
                    } else {
                        throw new IllegalArgumentException("Invalid response time value for " + prefix);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid response time format for " + prefix);
                }
            } else {
                config = new ESConfig(name, url);
            }
        } else {
            throw new IllegalArgumentException("Missing properties for " + prefix);
        }
    }

    /**
     * Initializes the admin settings.
     */
    private void initAdminSettings() {
        String adminEmail = prop.getProperty("Admin_EMAIL");
        String adminPassword = prop.getProperty("Admin_PASSWORD");

        if (adminEmail != null && adminPassword != null) {
            initialAdmin = new Admin(ADMIN_START_ID, adminEmail, adminPassword);
        } else {
            throw new IllegalArgumentException("Missing admin properties");
        }
    }

    /**
     * Retrieves the database settings.
     *
     * @return The properties containing the database settings.
     */
    public Properties getDBSetting() {
        return DBSetting;
    }

    /**
     * Retrieves the supply settings for the external service.
     *
     * @return The supply ESConfig.
     */
    public ESConfig getSupplyConfig() {
        return supplyConfig;
    }

    /**
     * Retrieves the payment settings for the external service.
     *
     * @return The payment ESConfig.
     */
    public ESConfig getPaymentConfig() {
        return paymentConfig;
    }

    /**
     * Retrieves the initial admin settings.
     *
     * @return The initial Admin object.
     */
    public Admin getInitialAdmin() {
        return initialAdmin;
    }
}
