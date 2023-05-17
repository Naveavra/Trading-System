package bridge;

import data.*;

import java.util.List;

public class ProxyBridge implements Bridge {
    private Bridge real = null;
    public void setRealBridge(Bridge implementation) {
        if (real == null)
            real = implementation;
    }

    @Override
    public int initTradingSystem() {
        if (real != null)
            return real.initTradingSystem();
        return -1;
    }

    @Override
    public int shutDownTradingSystem() {
        if (real != null)
            return real.shutDownTradingSystem();
        return -1;
    }

    @Override
    public int addAdmin(int adminId, String email, String password) {
        if (real != null)
            return real.addAdmin(adminId, email, password);
        return -1;
    }

    @Override
    public int register(String email, String password, String birthday) {
        if (real != null)
            return real.register(email, password, birthday);
        return -1;
    }

    @Override
    public int login(String email, String password) {
        if (real != null)
            return real.login(email, password);
        return -1;
    }

    @Override
    public int logout(int user) {
        if (real != null)
            return real.logout(user);
        return -1;
    }

    @Override
    public int createStore(int user, String description) {
        if (real != null)
            return real.createStore(user, description);
        return -1;
    }

    @Override
    public List<String> getPossibleExternalSupplierService(int admin) {
        if (real != null)
            return real.getPossibleExternalSupplierService(admin);
        return null;
    }

    @Override
    public List<String> getAvailableExternalSupplierService(int user) {
        if (real != null)
            return real.getPossibleExternalSupplierService(user);
        return null;
    }

    @Override
    public int addExternalSupplierService(int admin, String esSupplier) {
        if (real != null)
            return real.addExternalSupplierService(admin, esSupplier);
        return -1;
    }

    @Override
    public int removeExternalSupplierService(int admin, String es) {
        if (real != null)
            return real.removeExternalSupplierService(admin, es);
        return -1;
    }

    @Override
    public int replaceExternalSupplierService(int admin, String esSupplier) {
        if (real != null)
            return real.replaceExternalSupplierService(admin, esSupplier);
        return -1;
    }

    @Override
    public List<String> getPossibleExternalPaymentService(int admin) {
        if (real != null)
            return real.getPossibleExternalPaymentService(admin);
        return null;
    }

    @Override
    public List<String> getAvailableExternalPaymentService(int user) {
        if (real != null)
            return real.getAvailableExternalPaymentService(user);
        return null;
    }

    @Override
    public int addExternalPaymentService(int admin, String esPayment) {
        if (real != null)
            return real.addExternalPaymentService(admin, esPayment);
        return -1;
    }

    @Override
    public int removeExternalPaymentService(int admin, String es) {
        if (real != null)
            return real.removeExternalPaymentService(admin, es);
        return -1;
    }

    @Override
    public int replaceExternalPaymentService(int admin, String esPayment) {
        if (real != null)
            return real.replaceExternalPaymentService(admin, esPayment);
        return -1;
    }

    @Override
    public List<MessageInfo> getUnreadMessages(String user) {
        if (real != null)
            return real.getUnreadMessages(user);
        return null;
    }

    @Override
    public List<MessageInfo> getAllMessages(int user) {
        if (real != null)
            return real.getAllMessages(user);
        return null;
    }

    @Override
    public int addProduct(int userId, int storeId, List<String> categories, String name, String description, int price, int quantity) {
        if (real != null)
            return real.addProduct(userId, storeId, categories, name, description, price, quantity);
        return -1;
    }

    @Override
    public int removeProduct(int user, int store, int product) {
        if (real != null)
            return real.removeProduct(user, store, product);
        return -1;
    }

    @Override
    public int updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity) {
        if (real != null)
            return real.updateProduct(userId, storeId, productId, categories, name, description, price, quantity);
        return -1;
    }

    @Override
    public List<ProductInfo> getProductsInStore(int store) {
        if (real != null)
            return real.getProductsInStore(store);
        return null;
    }

    @Override
    public int changeDiscountPolicy(int userId, int storeId, String policy) {
        if (real != null)
            return real.changeDiscountPolicy(userId, storeId, policy);
        return -1;
    }

    @Override
    public int changePurchasePolicy(int userId, int storeId, String policy) {
        if (real != null)
            return real.changeDiscountPolicy(userId, storeId, policy);
        return -1;
    }

    @Override
    public int appointmentOwnerInStore(int user, int store, int owner) {
        if (real != null)
            return real.appointmentOwnerInStore(user, store, owner);
        return -1;
    }

    @Override
    public int appointmentMangerInStore(int user, int store, int manger) {
        if (real != null)
            return real.appointmentMangerInStore(user, store, manger);
        return -1;
    }

    @Override
    public int closeStore(int user, int store) {
        if (real != null)
            return real.closeStore(user, store);
        return -1;
    }

    @Override
    public int reopenStore(int user, int store) {
        if (real != null)
            return real.reopenStore(user, store);
        return -1;
    }

    @Override
    public List<PositionInfo> getPositionInStore(int user, int store) {
        if (real != null)
            return real.getPositionInStore(user, store);
        return null;
    }

    @Override
    public int addStoreManagerPermissions(int user, int store, int managerId, int permissionsIds) {
        if (real != null)
            return real.addStoreManagerPermissions(user, store, managerId, permissionsIds);
        return -1;
    }

    @Override
    public int removeStoreManagerPermissions(int user, int store, int managerId, int permissionsIds) {
        if (real != null)
            return real.removeStoreManagerPermissions(user, store, managerId, permissionsIds);
        return -1;
    }

    @Override
    public int addStoreManagerPermissions(int user, int store, int managerId, List<Integer> permissionsIds) {
        if (real != null)
            return real.addStoreManagerPermissions(user, store, managerId, permissionsIds);
        return -1;
    }



    @Override
    public PermissionInfo getManagerPermissionInStore(int user, int store, int manager) {
        if (real != null)
            return real.getManagerPermissionInStore(user, store, manager);
        return null;
    }

    @Override
    public List<PurchaseInfo> getStorePurchasesHistory(int user, int store) {
        if (real != null)
            return real.getStorePurchasesHistory(user, store);
        return null;
    }

    @Override
    public List<PurchaseInfo> getBuyerPurchasesHistory(int user, int buyer) {
        if (real != null)
            return real.getBuyerPurchasesHistory(user, buyer);
        return null;
    }

    @Override
    public List<AdminInfo> getAllAdmins(int adminId) {
        if (real != null)
            return real.getAllAdmins(adminId);
        return null;
    }

    @Override
    public List<StoreInfo> getAllStores() {
        if (real != null)
            return real.getAllStores();
        return null;
    }

    @Override
    public int removeAdmin(int adminId) {
        if (real != null)
            return real.removeAdmin(adminId);
        return -1;
    }

    @Override
    public StoreInfo getStore(int storeId) {
        if (real != null)
            return real.getStore(storeId);
        return null;
    }

    @Override
    public List<ProductInfo> getProductInStore(int storeId) {
        if (real != null)
            return real.getProductInStore(storeId);
        return null;
    }

    @Override
    public int adminLogin(String email, String password) {
        if (real != null)
            return real.adminLogin(email, password);
        return -1;
    }

    @Override
    public int adminLogout(int admin) {
        if (real != null)
            return real.adminLogout(admin);
        return -1;
    }

    @Override
    public int enterSystem() {
        if (real != null)
            return real.enterSystem();
        return -1;
    }

    @Override
    public int addProductToCart(int user, int store, int productID, int quantity) {
        if (real != null)
            return real.addProductToCart(user, store, productID, quantity);
        return -1;
    }

    @Override
    public CartInfo getCart(int user) {
        if (real != null)
            return real.getCart(user);
        return null;
    }

    @Override
    public int makePurchase(int user, String accountNumber) {
        if (real != null)
            return real.makePurchase(user, accountNumber);
        return -1;
    }

    @Override
    public int exitSystem(int guestId) {
        if (real != null)
            return real.exitSystem(guestId);
        return -1;
    }

    @Override
    public int removeProductFromCart(int userId, int storeId, int productId) {
        if (real != null)
            return real.removeProductFromCart(userId, storeId, productId);
        return -1;
    }

    @Override
    public int changeQuantityInCart(int userId, int storeId, int productId, int change) {
        if (real != null)
            return real.changeQuantityInCart(userId, storeId, productId, change);
        return -1;
    }

    @Override
    public List<String> getNotifications(int userId) {
        if(real != null)
            return real.getNotifications(userId);
        return null;
    }
}
