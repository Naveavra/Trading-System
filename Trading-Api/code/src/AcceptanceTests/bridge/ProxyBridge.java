package bridge;

import data.*;
import org.json.JSONObject;
import service.ExternalService.Payment.PaymentAdapter;
import service.ExternalService.Supplier.SupplierAdapter;
import utils.messageRelated.Notification;
import utils.stateRelated.Role;

import java.util.List;

public class ProxyBridge implements Bridge {
    private Bridge real = null;
    public void setRealBridge(Bridge implementation) {
        if (real == null)
            real = implementation;
    }

    @Override
    public boolean initTradingSystem() {
        if (real != null)
            return real.initTradingSystem();
        return false;
    }

    @Override
    public boolean shutDownTradingSystem() {
        if (real != null)
            return real.shutDownTradingSystem();
        return false;
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
    public LoginData loginAndGetData(String email, String password) {
        if (real != null)
            return real.loginAndGetData(email, password);
        return null;
    }

    @Override
    public int logout(int user) {
        if (real != null)
            return real.logout(user);
        return -1;
    }

    @Override
    public int createStore(int user, String name, String description, String img) {
        if (real != null)
            return real.createStore(user, name, description, img);
        return -1;
    }

    @Override
    public List<String> getPossibleExternalSupplierService(int admin) {
        if (real != null)
            return real.getPossibleExternalSupplierService(admin);
        return null;
    }

    @Override
    public List<String> getAvailableExternalSupplierService() {
        if (real != null)
            return real.getAvailableExternalSupplierService();
        return null;
    }

    @Override
    public boolean addExternalSupplierService(int admin, String esSupplier) {
        if (real != null)
            return real.addExternalSupplierService(admin, esSupplier);
        return false;
    }

    @Override
    public boolean addExternalSupplierService(int adminId, String esSupplier, SupplierAdapter supplierAdapter) {
        if (real != null)
            return real.addExternalSupplierService(adminId, esSupplier, supplierAdapter);
        return false;
    }

    @Override
    public boolean removeExternalSupplierService(int admin, String es) {
        if (real != null)
            return real.removeExternalSupplierService(admin, es);
        return false;
    }

    @Override
    public boolean replaceExternalSupplierService(int admin, String esSupplier) {
        if (real != null)
            return real.replaceExternalSupplierService(admin, esSupplier);
        return false;
    }

    @Override
    public List<String> getPossibleExternalPaymentService(int user) {
        if (real != null)
            return real.getPossibleExternalPaymentService(user);
        return null;
    }

    @Override
    public List<String> getAvailableExternalPaymentService() {
        if (real != null)
            return real.getAvailableExternalPaymentService();
        return null;
    }

    @Override
    public boolean addExternalPaymentService(int admin, String esPayment) {
        if (real != null)
            return real.addExternalPaymentService(admin, esPayment);
        return false;
    }

    @Override
    public boolean addExternalPaymentService(int adminId, String esPayment, PaymentAdapter paymentAdapter) {
        if (real != null)
            return real.addExternalPaymentService(adminId, esPayment, paymentAdapter);
        return false;
    }

    @Override
    public boolean removeExternalPaymentService(int admin, String es) {
        if (real != null)
            return real.removeExternalPaymentService(admin, es);
        return false;
    }

    @Override
    public boolean replaceExternalPaymentService(int admin, String esPayment) {
        if (real != null)
            return real.replaceExternalPaymentService(admin, esPayment);
        return false;
    }

    @Override
    public List<String> getPaymentServicesPossibleOptions(int adminId) {
        if (real != null)
            return real.getPaymentServicesPossibleOptions(adminId);
        return null;
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
    public int addProduct(int userId, int storeId, List<String> categories, String name, String description, int price,
                          int quantity, String img) {
        if (real != null)
            return real.addProduct(userId, storeId, categories, name, description, price, quantity, img);
        return -1;
    }

    @Override
    public boolean removeProduct(int user, int store, int product) {
        if (real != null)
            return real.removeProduct(user, store, product);
        return false;
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
    public int appointmentOwnerInStore(int user, int store, String owner) {
        if (real != null)
            return real.appointmentOwnerInStore(user, store, owner);
        return -1;
    }

    @Override
    public int appointmentMangerInStore(int user, int store, String manger) {
        if (real != null)
            return real.appointmentMangerInStore(user, store, manger);
        return -1;
    }

    @Override
    public boolean closeStore(int user, int store) {
        if (real != null)
            return real.closeStore(user, store);
        return false;
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
    public boolean removeStoreManagerPermissions(int user, int store, int managerId, List<Integer> permissionsIds) {
        if (real != null)
            return real.removeStoreManagerPermissions(user, store, managerId, permissionsIds);
        return false;
    }

    @Override
    public boolean addStoreManagerPermissions(int user, int store, int managerId, List<Integer> permissionsIds) {
        if (real != null)
            return real.addStoreManagerPermissions(user, store, managerId, permissionsIds);
        return false;
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
    public int makePurchase(int user, JSONObject payment, JSONObject supplier) {
        if (real != null)
            return real.makePurchase(user, payment, supplier);
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
    public List<Notification> getNotifications(int userId) {
        if(real != null)
            return real.getNotifications(userId);
        return null;
    }

    @Override
    public StoreInfo getStoreInfo(int storeId) {
        if(real != null)
            return real.getStoreInfo(storeId);
        return null;
    }

    @Override
    public Role getRoleInStore(int userId, int workerId, int storeId) {
        if(real != null)
            return real.getRoleInStore(userId, workerId, storeId);
        return null;
    }
}
