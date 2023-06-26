package server.Config;

import database.HibernateUtil;
import market.Admin;
import org.hibernate.cfg.Environment;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * The ConfigParser class is responsible for parsing the config.properties file and initializing various settings.
 */
public class ConfigParser {
    private static ConfigParser instance;
    private final JSONObject jsonConfig;
    private Properties DBSetting;
    private ESConfig supplyConfig;
    private ESConfig paymentConfig;
    private Admin initialAdmin;
    private final int ADMIN_START_ID = 1;
    private final String configFilePath;
    private ConnectionDetails connectionDetails;

    /**
     * Private constructor to prevent direct instantiation.
     *
     * @param configFilePath The path to the config.json file.
     */
    private ConfigParser(String configFilePath) {
        this.configFilePath = configFilePath;
        jsonConfig = loadJsonConfig();
    }

    /**
     * Returns the singleton instance of ConfigParser.
     *
     * @param configFilePath The path to the config.json file (optional if instance already exists).
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
            String configFilePath = "../../config1.json";
            System.out.println(configFilePath);
            instance = new ConfigParser(configFilePath);
        }
        return instance;
    }

    /**
     * Loads the JSON config from the file.
     *
     * @return The loaded JSON config.
     */
    private JSONObject loadJsonConfig() {
        try {
            FileInputStream inputStream = new FileInputStream(configFilePath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, "UTF-8");
            return new JSONObject(jsonString);
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found: " + configFilePath);
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Failed to read config file: " + configFilePath);
            System.exit(-1);
        }
        return null;
    }

    /**
     * Initializes all the settings from the loaded JSON config.
     */
    public void initSettings() {
        try {
            initDBSettings();
            initAdminSettings();
            initServerSettings();
            supplyConfig = initESConfigSettings("Supply");
            paymentConfig = initESConfigSettings("Payment");
        }
        catch (Exception e)
        {
            System.out.println("""
                                Config file structure is illegal!:
                    {
                        "Database": {
                          "DB_DRIVER": "com.mysql.cj.jdbc.Driver",
                          "DB_URL": "jdbc:mysql://<ip address>/<db_name>",
                          "DB_USER": "root",
                          "DB_PASS": "<db password>",
                          "DB_SHOW_SQL": "true",
                          "DB_CURRENT_SESSION_CONTEXT_CLASS": "thread",
                          "DB_HBM2DDL_AUTO": "update"
                        },
                        "ExternalService": {
                          "Payment": {
                           "NAME": <Supplier name>,
                            "URL": <Service url>,
                            "RESPONSE_TIME": <response time>
                          },
                          "Supply": {
                            "NAME": <Supplier name>,
                            "URL": <Service url>,
                            "RESPONSE_TIME": <response time>
                          }
                        },
                        "Server_Back": {
                        "IP": <ip address>,
                        "Port": <port number>
                        },
                        "Server_Front": {
                        "Port": <port number>
                        },
                        "Admin": {
                          "EMAIL": "<admin mail>",
                          "PASSWORD": "<legal password>"
                        }
                      }
                    }
                                """);
            System.exit(-1);
        }

    }

    /**
     * Initializes the server settings based on the configuration file.
     * Reads the IP address and port number from the configuration file.
     * Throws an error if the IP address or port is invalid.
     */
    private void initServerSettings() {
        JSONObject serverConfig = jsonConfig.getJSONObject("Server_Back");
        String ipAddress = serverConfig.getString("IP");
        int port;
        try {
            port = serverConfig.getInt("Port");
        } catch (JSONException e) {
            throw new IllegalArgumentException("Invalid value for port: " + serverConfig.get("Port"), e);
        }
        connectionDetails = new ConnectionDetails(ipAddress, port);
    }

    /**
     * Initializes the database settings.
     */
    private void initDBSettings() {
        JSONObject dbConfig = jsonConfig.getJSONObject("Database");
        String dbDriver = dbConfig.getString("DB_DRIVER");
        String dbURL = dbConfig.getString("DB_URL");
        String dbUser = dbConfig.getString("DB_USER");
        String dbPassword = dbConfig.getString("DB_PASS");
        String showSQL = dbConfig.getString("DB_SHOW_SQL");
        String sessionContextClass = dbConfig.getString("DB_CURRENT_SESSION_CONTEXT_CLASS");
        String hbm2ddlAuto = dbConfig.getString("DB_HBM2DDL_AUTO");

        DBSetting = new Properties();
        DBSetting.put(Environment.DRIVER, dbDriver);
        DBSetting.put(Environment.URL, dbURL);
        DBSetting.put(Environment.USER, dbUser);
        DBSetting.put(Environment.PASS, dbPassword);
        DBSetting.put(Environment.SHOW_SQL, showSQL);
        DBSetting.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, sessionContextClass);
        DBSetting.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);

        HibernateUtil.setSettings();
    }

    /**
     * Initializes the external service configuration settings.
     *
     * @param esType The external service for the properties (e.g., "Supply" or "Payment").
     * @return The initialized ESConfig object.
     */
    private ESConfig initESConfigSettings(String esType) {
        JSONObject es = jsonConfig.getJSONObject("ExternalService");
        JSONObject esConfig = es.getJSONObject(esType);
        try{
            String name = esConfig.getString("NAME");
            String url = esConfig.getString("URL");
            int responseTime;
            try {
                responseTime = esConfig.getInt("RESPONSE_TIME");
            } catch (JSONException e) {
                throw new IllegalArgumentException("Invalid value for response time: " + esConfig.get("RESPONSE_TIME"), e);
            }
            return new ESConfig(name, url, responseTime);
        }
        catch (Exception e)
        {
            return new ESConfig();
        }
    }

    /**
     * Initializes the admin settings.
     */
    private void initAdminSettings() {
        JSONObject adminConfig = jsonConfig.getJSONObject("Admin");
        String adminEmail = adminConfig.getString("EMAIL");
        String adminPassword = adminConfig.getString("PASSWORD");
        initialAdmin = new Admin(1, adminEmail, adminPassword);
    }

    /**
     * Retrieves the database settings.
     *
     * @return The properties containing the database settings.
     */
    public Properties getDBSetting() {
        if(DBSetting == null)
            initDBSettings();
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
