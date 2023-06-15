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
    private static ConfigParser instance;
    private final Properties prop = new Properties();
    private Properties DBSetting;
    private ESConfig supplyConfig;
    private ESConfig paymentConfig;
    private Admin initialAdmin;
    private final int ADMIN_START_ID = 1;
    private FileInputStream input = null;
    private final String configFilePath;
    private ConnectionDetails connectionDetails;

    /**
     * Private constructor to prevent direct instantiation.
     *
     * @param configFilePath The path to the config.properties file.
     */
    private ConfigParser(String configFilePath) {
        this.configFilePath = configFilePath;
        loadProperties();
        initSettings();
    }

    /**
     * Returns the singleton instance of ConfigParser.
     *
     * @param configFilePath The path to the config.properties file (optional if instance already exists).
     * @return The ConfigParser instance.
     */
    public static ConfigParser getInstance(String configFilePath) {
        if (instance == null) {
            instance = new ConfigParser(configFilePath);
        }
        return instance;
    }

    /**
     * Returns the singleton instance of ConfigParser.
     * Uses the previously provided config file path.
     *
     * @return The ConfigParser instance.
     * @throws IllegalStateException If the config file path was not provided during the first instantiation.
     */
    public static ConfigParser getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ConfigParser instance has not been initialized with a config file path.");
        }
        return instance;
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
        initAdminSettings();
        initServerSettings();
        initDBSettings();
        supplyConfig = initESConfigSettings("ES_S");
        paymentConfig = initESConfigSettings("ES_P");
    }


    /**
     * Initializes the server settings based on the configuration file.
     * Reads the IP address and port number from the configuration file.
     * Throws an error if the IP address or port is invalid.
     */
    private void initServerSettings() throws IllegalArgumentException {
        String ipAddressKey = "Server_B_IP";
        String portKey = "Server_B_Port";

        // Read the IP address from the configuration file
        String ipAddress = prop.getProperty(ipAddressKey);
        if (ipAddress == null || ipAddress.isEmpty()) {
            throw new IllegalArgumentException("Invalid IP address configuration: " + ipAddressKey);
        }

        // Read the port number from the configuration file
        String portValue = prop.getProperty(portKey);
        if (portValue == null || portValue.isEmpty()) {
            throw new IllegalArgumentException("Invalid port configuration: " + portKey);
        }

        int port;
        try {
            port = Integer.parseInt(portValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port number: " + portValue);
        }
        connectionDetails = new ConnectionDetails(ipAddress, port);
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
     * @return
     */
    private ESConfig initESConfigSettings(String prefix) {
        String name = prop.getProperty(prefix + "_NAME");
        String url = prop.getProperty(prefix + "_URL");
        String responseTime = prop.getProperty(prefix + "_RESPONSE_TIME");

        if (name != null && url != null) {
            if (responseTime != null) {
                try {
                    int time = Integer.parseInt(responseTime);
                    if (time >= 0) {
                        return new ESConfig(name, url, time);
                    } else {
                        throw new IllegalArgumentException("Invalid response time value for " + prefix);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid response time format for " + prefix);
                }
            } else {
                return new ESConfig(name, url);
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

    /**
     * Retrieves the server settings.
     *
     * @return The server ConnectionDetails object.
     */
    public ConnectionDetails getServerConnectionDetails() {
        return connectionDetails;
    }
}
