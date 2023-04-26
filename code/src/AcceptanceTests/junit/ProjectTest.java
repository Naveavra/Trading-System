package junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bridge.Bridge;
import bridge.Driver;
import data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public abstract class ProjectTest{
    private Bridge bridge;

    // SetUp information

    // Init System:


    // Store, admin, appointment
    AdminInfo mainAdmin = new AdminInfo(-1,"admin@gmail.com", "adminA1");
    public static final int USER_EMAIL = 0, USER_USER = 1, USER_PASS = 2, USER_BIRTHDAY = 3;
    public static final int ADMIN_USER = 0, ADMIN_PASS = 1;
    protected final String[][] admins = {{"admin1@gmail.com", "admin1A"}};
    protected final String[][] users = {{"hello@gmail.com", "hello", "hello1A", "01/01/2002"}, {"user@gmail.com", "user1A", "1234567891Ad", "01/01/2002"},
            {"user1@gmail.com", "user1", "abc123456dE", "01/01/2002"}};

    protected HashMap<String, AdminInfo> admins_dict = new HashMap();
    protected HashMap<String, UserInfo> users_dict = new HashMap();
    protected List<StoreInfo> stores = new ArrayList<>();
    protected ProductInfo pi5s4;

    @BeforeEach
    public void setUp() {
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
        for (String[] adminInfo : admins) {
            AdminInfo ai = new AdminInfo(adminInfo[ADMIN_USER], adminInfo[ADMIN_PASS]);
            this.bridge.addAdmin(mainAdmin.getAdminId(), adminInfo[ADMIN_USER], adminInfo[ADMIN_PASS]);
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
        StoreInfo newStore0 = new StoreInfo(ui.getUserId(), "Phone Store");
        StoreInfo newStore1 = new StoreInfo(ui.getUserId(), "Sport Store");
        StoreInfo newStore2 = new StoreInfo(ui.getUserId(), "Cloth Store");
        StoreInfo newStore3 = new StoreInfo(ui.getUserId(), "Kids Store");
        StoreInfo newStore4 = new StoreInfo(ui1.getUserId(), "Phone Store");
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

    // Auxiliary procedures

    protected ProductInfo createProduct0() {
        // Good product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Apple");
        ProductInfo newProduct = new ProductInfo("IPhone", "IPhone11 Appple", categories, 2000, 20);
        return newProduct;
    }


    protected ProductInfo createProduct1() {
        // Good product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Samsung");
        ProductInfo newProduct = new ProductInfo("Galaxy 1", "Samsung Galaxy 1", categories, 500, 20);
        return newProduct;
    }

    protected ProductInfo createProduct2() {
        // Bad product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Apple");
        ProductInfo newProduct = new ProductInfo("IPhone", "IPhone12 Appple", categories, -100, 20);
        return newProduct;
    }

    protected ProductInfo createProduct3() {
        // Bad product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Samsung");
        ProductInfo newProduct = new ProductInfo("Galaxy 1", "Samsung Galaxy 1", categories, 500, 0);
        return newProduct;
    }

    protected ProductInfo createProduct5() {
        // Good product
        List<String> categories = new ArrayList<>();
        categories.add("Smartphones");
        categories.add("Apple");
        ProductInfo newProduct = new ProductInfo("IPhone", "IPhone11 Appple", categories, 2000, 1);
        return newProduct;
    }

    public int createStore(StoreInfo si) {
        return bridge.createStore(si.getCreatorId(), si.getDescription());
    }
    public int createStore(int creatorId, String storeDesc) {
        return bridge.createStore(creatorId, storeDesc);
    }

    public int removeProduct(int user, int store, int productId) {
        return bridge.removeProduct(user, store, productId);
    }

    public int addProduct(int userId, int storeId, List<String> categories, String name, String description, int price, int quantity)
    {
        return bridge.addProduct(userId, storeId, categories, name, description, price, quantity);
    }
    public int addProduct(int userId, int storeId, ProductInfo pi)
    {
        return bridge.addProduct(userId, storeId, pi.getCategories(), pi.getName(), pi.getDescription(), pi.getPrice(), pi.getQuantity());
    }

    public int updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity)
    {
        return bridge.updateProduct(userId, storeId, productId, categories, name, description, price, quantity);
    }

    public int changeDiscountPolicy(int user, int store, String policy) {
        return bridge.changeDiscountPolicy(user, store, policy);
    }

    public int appointmentOwnerInStore(int user, int store, int newOwnerID)
    {
        return bridge.appointmentOwnerInStore(user, store, newOwnerID);
    }

    public int appointmentMangerInStore(int user, int store, int newManagerID)
    {
        return bridge.appointmentMangerInStore(user, store, newManagerID);
    }

    public int changePurchasePolicy(int user, int store, String policy)
    {
        return bridge.changePurchasePolicy(user, store, policy);
    }

    public int changeStoreManagerPermissions(int user, int store, int manager, List<Integer> permissionsIds) {
        return bridge.changeStoreManagerPermissions(user, store, manager, permissionsIds);
    }

    public int closeStore(int user, int store) {
        return bridge.closeStore(user, store);
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

    public List<PurchaseInfo> getBuyerPurchasesHistory(int user, int store) {
        return bridge.getStorePurchasesHistory(user, store);
    }

    public List<PermissionInfo> getManagerPermissionInStore(int user, int store, int manager) {
        return bridge.getManagerPermissionInStore(user, store, manager);
    }

    public int register(String mail, String pass, String birthday) {
        return bridge.register(mail, pass, birthday);
    }

    public int login(String email, String password) {
        return bridge.login(email, password);
    }

    public int adminLogin(String email, String password) {
        return bridge.adminLogin(email, password);
    }
    public int adminLogout(int admin) {
        return bridge.adminLogout(admin);
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

    public List<ProductInfo> getProductInStore(int storeId)
    {
        return bridge.getProductInStore(storeId);
    }

    public int addExternalPaymentService(int adminId, int esPayment)
    {
        return bridge.addExternalPaymentService(adminId, esPayment);
    }

    public int removeExternalPaymentService(int adminId, int esPayment)
    {
        return bridge.removeExternalPaymentService(adminId, esPayment);
    }

    public int replaceExternalPaymentService(int adminId, int es, int esPayment)
    {
        return bridge.replaceExternalPaymentService(adminId, es, esPayment);
    }

    public int addExternalSupplierService(int adminId, int esPayment)
    {
        return bridge.addExternalSupplierService(adminId, esPayment);
    }

    public int removeExternalSupplierService(int adminId, int esPayment)
    {
        return bridge.removeExternalSupplierService(adminId, esPayment);
    }

    public int replaceExternalSupplierService(int adminId, int es, int esPayment)
    {
        return bridge.replaceExternalSupplierService(adminId, es, esPayment);
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

    public int makePurchase(int user, String accountNumber)
    {
        return bridge.makePurchase(user, accountNumber);
    }

    public int removeProductFromCart(int userId,  int storeId, int productId)
    {
        return bridge.removeProductFromCart(userId, storeId, productId);
    }
    public int changeQuantityInCart(int userId, int storeId, int productId, int change)
    {
        return bridge.changeQuantityInCart(userId, storeId, productId, change);
    }
}