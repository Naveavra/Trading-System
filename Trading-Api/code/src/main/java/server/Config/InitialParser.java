package server.Config;

import database.HibernateUtil;
import org.json.JSONObject;
import org.json.JSONArray;

import server.API;
import server.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InitialParser {
    private static InitialParser instance;
    private final String initialFilePath;
    private final JSONObject jsonInitial;
    private ConnectionDetails connectionDetails;

    private InitialParser(String configFilePath) {
        this.initialFilePath = configFilePath;
        jsonInitial = loadJsonConfig();
        //initUseCases();
    }
    public static InitialParser getInstance(String configFilePath) {
        if (instance == null) {
            instance = new InitialParser(configFilePath);
        }
        return instance;
    }
    private JSONObject loadJsonConfig() {
        try {
            FileInputStream inputStream = new FileInputStream(initialFilePath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, "UTF-8");
            return new JSONObject(jsonString);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file not found: " + initialFilePath, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config file: " + initialFilePath, e);
        }
    }
    public void initUseCases() {
        HibernateUtil.createDrop = true;
        Server.api = new API(Server.configs);
        JSONArray commands = jsonInitial.getJSONArray("commands");
        String token = Server.api.getTokenForTest();

        for (int i = 0; i < commands.length(); i++) {
            JSONObject commandObject = commands.getJSONObject(i);
            String command = commandObject.getString("command");
            JSONObject params = commandObject.getJSONObject("params");

            switch (command) {
                case "REGISTER":
                    String email = params.getString("userName");
                    String password = params.getString("password");
                    String birthday = params.getString("birthday");
                    Server.api.register(email, password, birthday);
                    break;
                case "LOGIN":
                    String loginEmail = params.getString("userName");
                    String loginPassword = params.getString("password");
                    Server.api.login(loginEmail, loginPassword);
                    break;
                case "OPEN_STORE":
                    int userId = params.getInt("userId");
                    String storeName = params.getString("name");
                    String description = params.getString("description");
                    String image = params.getString("img");
                    Server.api.openStore(userId, token, storeName, description, image);
                    break;
                case "APPOINT_STORE_OWNER":
                    int appointOwnerId = params.getInt("userId");
                    String ownerEmail = params.getString("emailOfUser");
                    int storeId = params.getInt("storeId");
                    Server.api.appointOwner(appointOwnerId, token, ownerEmail, storeId);
                    break;
                case "APPOINT_STORE_MANAGER":
                    int appointManagerId = params.getInt("userId");
                    String managerEmail = params.getString("managerToAppoint");
                    int storeIdManager = params.getInt("storeId");
                    Server.api.appointManager(appointManagerId, token, managerEmail, storeIdManager);
                    break;
                case "LOGOUT":
                    int logoutUserId = params.getInt("userId");
                    Server.api.logout(logoutUserId);
                    break;
                case "ADD_ADMIN":
                    int adminId = params.getInt("adminId");
                    String newAdminEmail = params.getString("email");
                    String passwordAdmin = params.getString("password");
                    Server.api.addAdmin(0, token,newAdminEmail,passwordAdmin);
                    break;
                case "ADD_ITEM_TO_STORE":
                    List<String> categories = new ArrayList<>();
                    int addItemUserId = params.getInt("userId");
                    int addItemStoreId = params.getInt("storeId");
                    JSONArray addItemCategories = params.getJSONArray("categories");
                    for (int j = 0; j < addItemCategories.length(); j++) {
                        categories.add(addItemCategories.getString(j));
                    }
                    String addItemName = params.getString("name");
                    String addItemDescription = params.getString("description");
                    int addItemPrice = params.getInt("price");
                    int addItemQuantity = params.getInt("quantity");
                    String addItemImg = params.getString("img");
                    Server.api.addProduct(addItemUserId, token, addItemStoreId, categories, addItemName, addItemDescription, addItemPrice, addItemQuantity, addItemImg);
                    break;
                default:
                    // Handle unsupported command
                    break;
            }
        }
    }
}