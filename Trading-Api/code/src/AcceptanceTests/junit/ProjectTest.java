package junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bridge.Bridge;
import bridge.Driver;
import data.*;
import database.Dao;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import utils.messageRelated.Notification;
import utils.stateRelated.Role;

import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class ProjectTest{
    private Bridge bridge;

    // SetUp information

    // Init System:


    // Store, admin, appointment
    AdminInfo mainAdmin = new AdminInfo(1,"admin@gmail.com", "admin1A");
    public static final int USER_EMAIL = 0, USER_USER = 1, USER_PASS = 2, USER_BIRTHDAY = 3;
    public static final int ADMIN_ID = 0, ADMIN_USER = 1, ADMIN_PASS = 2;
    protected final String[][] admins = {{"2","admin1@gmail.com", "admin1A"}};
    protected final String[][] users = {{"hello@gmail.com", "hello", "hello1A", "01/01/2002"}, {"user@gmail.com", "user1A", "1234567891Ad", "01/01/2002"},
            {"user1@gmail.com", "user1", "abc123456dE", "01/01/2002"}};

    protected HashMap<String, AdminInfo> admins_dict = new HashMap();
    protected HashMap<String, UserInfo> users_dict = new HashMap();
    protected List<StoreInfo> stores = new ArrayList<>();
    protected ProductInfo pi5s4;

    @BeforeEach
    public void setUp() {
        Dao.setForTests(true);
        this.bridge = Driver.getBridge();
        setUpTradingSystem();
        setUpAdmins();
        setUpUsers();
        setUpStores();
    }

    @AfterEach
    void tearDown() {
        tearDownTradingSystem();
    }

    private void setUpTradingSystem() {
        this.bridge.initTradingSystem();
    }

    private void tearDownTradingSystem() {
        this.bridge.shutDownTradingSystem();
    }

    private void setUpAdmins() {
        mainAdmin.setAdminId(adminLogin(mainAdmin.getEmail(), mainAdmin.getPassword()));
        for (String[] adminInfo : admins) {
            this.bridge.addAdmin(mainAdmin.getAdminId(), adminInfo[ADMIN_USER], adminInfo[ADMIN_PASS]);
            AdminInfo ai = new AdminInfo(Integer.parseInt(adminInfo[ADMIN_ID]), adminInfo[ADMIN_USER], adminInfo[ADMIN_PASS]);
            admins_dict.put(ai.getEmail(), ai);
        }
        admins_dict.put(mainAdmin.getEmail(), mainAdmin);
    }

    private void setUpUsers() {
        for (String[] userInfo : users) {
            UserInfo ui = new UserInfo(userInfo[USER_EMAIL], userInfo[USER_USER], userInfo[USER_PASS], userInfo[USER_BIRTHDAY]);
            register(userInfo[USER_EMAIL], userInfo[USER_PASS], userInfo[USER_BIRTHDAY]);
            users_dict.put(ui.getEmail(), ui);
        }
    }

    private void setUpStores() {
        UserInfo ui = users_dict.get(users[0][0]);
        UserInfo ui1 = users_dict.get(users[1][0]);
        //Login:
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        ui1.setUserId(login(ui1.getEmail(), ui1.getPassword()));
        //Create Stores:
        StoreInfo newStore0 = new StoreInfo(ui.getUserId(), "Apple", "Phone Store", "img");
        StoreInfo newStore1 = new StoreInfo(ui.getUserId(), "nike", "Sport Store", "img");
        StoreInfo newStore2 = new StoreInfo(ui.getUserId(), "store3", "Cloth Store", "img");
        StoreInfo newStore3 = new StoreInfo(ui.getUserId(), "toysRus", "Kids Store", "img");
        StoreInfo newStore4 = new StoreInfo(ui1.getUserId(), "samsung", "Phone Store", "img");
        newStore0.setStoreId(createStore(newStore0));
        stores.add(newStore0);
        newStore1.setStoreId(createStore(newStore1));
        stores.add(newStore1);
        newStore2.setStoreId(createStore(newStore2));
        stores.add(newStore2);
        newStore3.setStoreId(createStore(newStore3));
        stores.add(newStore3);
        newStore4.setStoreId(createStore(newStore4));
        stores.add(newStore4);
        //Add product to store 4
        pi5s4 = createProduct5();
        pi5s4.setProductId(addProduct(ui1.getUserId(), newStore4.getStoreId(), pi5s4));

        logout(ui.getUserId());
        logout(ui1.getUserId());
    }

    protected JSONObject createPaymentJson()
    {
        JSONObject payment = new JSONObject();
        payment.put("payment_service", "WSEP");
        payment.put("cardNumber", "123456789");
        payment.put("month", "01");
        payment.put("year", "30");
        payment.put("holder", "Israel Visceral");
        payment.put("ccv", "000");
        payment.put("id", "123456789");
        return payment;
    }

    protected  JSONObject createSupplierJson()
    {
        JSONObject supplier = new JSONObject();
        supplier.put("supply_service", "WSEP");
        supplier.put("name", "Israel Visceral");
        supplier.put("address", "Reger 17");
        supplier.put("city", "Beer Sheva");
        supplier.put("country", "Israel");
        supplier.put("zip", "700000");
        return supplier;
    }

    // Auxiliary procedures

    protected ProductInfo createProduct0() {
        // Good product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Apple");
        ProductInfo newProduct = new ProductInfo("IPhone", "IPhone11 Appple", categories, 2000, 20, "img");
        return newProduct;
    }


    protected ProductInfo createProduct1() {
        // Good product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Samsung");
        ProductInfo newProduct = new ProductInfo("Galaxy 1", "Samsung Galaxy 1", categories, 500, 20, "img");
        return newProduct;
    }

    protected ProductInfo createProduct2() {
        // Bad product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Apple");
        ProductInfo newProduct = new ProductInfo("IPhone", "IPhone12 Appple", categories, -100, 20, "img");
        return newProduct;
    }

    protected ProductInfo createProduct3() {
        // Bad product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Samsung");
        ProductInfo newProduct = new ProductInfo("Galaxy 1", "Samsung Galaxy 1", categories, 500, 0, "img");
        return newProduct;
    }

    protected ProductInfo createProduct5() {
        // Good product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Apple");
        ProductInfo newProduct = new ProductInfo("IPhone", "IPhone11 Appple", categories, 2000, 1, "img");
        return newProduct;
    }

    public int createStore(StoreInfo si) {
        return bridge.createStore(si.getCreatorId(), si.getStoreName(), si.getDescription(), si.getImg());
    }
    public int createStore(int creatorId, String name, String storeDesc, String img) {
        return bridge.createStore(creatorId, name, storeDesc, img);
    }

    public boolean removeProduct(int user, int store, int productId) {
        return bridge.removeProduct(user, store, productId);
    }

    public int addProduct(int userId, int storeId, List<String> categories, String name, String description, int price,
                          int quantity, String img)
    {
        return bridge.addProduct(userId, storeId, categories, name, description, price, quantity, img);
    }
    public int addProduct(int userId, int storeId, ProductInfo pi)
    {
        return bridge.addProduct(userId, storeId, pi.getCategories(), pi.getName(), pi.getDescription(), pi.getPrice(), pi.getQuantity(),
                pi.getImg());
    }

    public int updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity)
    {
        return bridge.updateProduct(userId, storeId, productId, categories, name, description, price, quantity);
    }

    public int changeDiscountPolicy(int user, int store, String policy) {
        return bridge.changeDiscountPolicy(user, store, policy);
    }

    public int appointmentOwnerInStore(int user, int store, String newOwnerEmail)
    {
        return bridge.appointmentOwnerInStore(user, store, newOwnerEmail);
    }

    public int appointmentManagerInStore(int user, int store, String newManager)
    {
        return bridge.appointmentMangerInStore(user, store, newManager);
    }

    public int changePurchasePolicy(int user, int store, String policy)
    {
        return bridge.changePurchasePolicy(user, store, policy);
    }

    public boolean addStoreManagerPermissions(int user, int store, int manager, List<Integer> permissionsIds) {
        return bridge.addStoreManagerPermissions(user, store, manager, permissionsIds);
    }

    public boolean removeStoreManagerPermissions(int user, int store, int manager, List<Integer> permissionsIds) {
        return bridge.removeStoreManagerPermissions(user, store, manager, permissionsIds);
    }

    public boolean closeStore(int user, int store) {
        return bridge.closeStore(user, store);
    }

    public List<Notification> getNotifications(int userId){
        return bridge.getNotifications(userId);
    }

    public int reopenStore(int user, int store) {
        return bridge.reopenStore(user, store);
    }

    public List<PositionInfo> getPositionInStore(int user, int store) {
        return bridge.getPositionInStore(user, store);
    }

    public List<PurchaseInfo> getStorePurchasesHistory(int user, int store) {
        return bridge.getStorePurchasesHistory(user, store);
    }

    public List<PurchaseInfo> getBuyerPurchasesHistory(int user, int buyer) {
        return bridge.getBuyerPurchasesHistory(user, buyer);
    }

    public PermissionInfo getManagerPermissionInStore(int user, int manager, int store) {
        return bridge.getManagerPermissionInStore(user, store, manager);
    }

    public int register(String mail, String pass, String birthday) {
        return bridge.register(mail, pass, birthday);
    }

    public int login(String email, String password) {
        return bridge.login(email, password);
    }

    public LoginData loginAndGetData(String email, String password) {
        return bridge.loginAndGetData(email, password);
    }

    public int adminLogin(String email, String password) {
        return bridge.login(email, password);
    }
    public int adminLogout(int admin) {
        return bridge.logout(admin);
    }

    public int logout(int user) {
        return bridge.logout(user);
    }

    //  Admin Functionality:

    public List<AdminInfo> getAllAdmins(int admin) {
        return bridge.getAllAdmins(admin);
    }

    public int addAdmin(int admin, String email, String password)
    {
        return bridge.addAdmin(admin, email, password);
    }

    public List<StoreInfo> getAllStores()
    {
        return bridge.getAllStores();
    }

    public int removeAdmin(int adminId)
    {
        return bridge.removeAdmin(adminId);
    }

    public StoreInfo getStore(int storeId)
    {
        return bridge.getStore(storeId);
    }

    public StoreInfo getStoreInfo(int storeId)
    {
        return bridge.getStoreInfo(storeId);
    }

    public List<ProductInfo> getProductInStore(int storeId)
    {
        return bridge.getProductInStore(storeId);
    }

    public int addExternalPaymentService(int adminId, String esPayment)
    {
        return bridge.addExternalPaymentService(adminId, esPayment);
    }

    public int removeExternalPaymentService(int adminId, String esPayment)
    {
        return bridge.removeExternalPaymentService(adminId, esPayment);
    }

    public int replaceExternalPaymentService(int adminId, String esPayment)
    {
        return bridge.replaceExternalPaymentService(adminId, esPayment);
    }

    public int addExternalSupplierService(int adminId, String esSupplier)
    {
        return bridge.addExternalSupplierService(adminId, esSupplier);
    }


    public int removeExternalSupplierService(int adminId, String esSupplier)
    {
        return bridge.removeExternalSupplierService(adminId, esSupplier);
    }

    public int replaceExternalSupplierService(int adminId, String esSupplier)
    {
        return bridge.replaceExternalSupplierService(adminId, esSupplier);
    }

    public int enterSystem()
    {
        return bridge.enterSystem();
    }

    public int exitSystem(int guestId)
    {
        return bridge.exitSystem(guestId);
    }

    public int addProductToCart(int user, int store, int productID, int quantity)
    {
        return bridge.addProductToCart(user, store, productID, quantity);
    }

    public CartInfo getCart(int user)
    {
        return bridge.getCart(user);
    }

    public int makePurchase(int user, JSONObject payment, JSONObject supply)
    {
        return bridge.makePurchase(user, payment, supply);
    }

    public int removeProductFromCart(int userId,  int storeId, int productId)
    {
        return bridge.removeProductFromCart(userId, storeId, productId);
    }
    public int changeQuantityInCart(int userId, int storeId, int productId, int change)
    {
        return bridge.changeQuantityInCart(userId, storeId, productId, change);
    }

    public List<String> getAvailableExternalSupplierService()
    {
        return bridge.getAvailableExternalSupplierService();
    }

    public List<String> getPossibleExternalSupplierService(int adminId)
    {
        return bridge.getPossibleExternalSupplierService(adminId);
    }

    public List<String> getAvailableExternalPaymentService()
    {
        return bridge.getAvailableExternalPaymentService();
    }

    public List<String> getPossibleExternalPaymentService(int adminId)
    {
        return bridge.getPossibleExternalPaymentService(adminId);
    }

    Role getRoleInStore(int userId, int workerId, int storeId)
    {
        return bridge.getRoleInStore(userId, workerId, storeId);
    }
}
