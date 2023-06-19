package bridge;

import data.*;
import org.json.JSONObject;
import service.ExternalService.Payment.PaymentAdapter;
import service.ExternalService.Supplier.SupplierAdapter;
import utils.messageRelated.Notification;
import utils.stateRelated.Role;

import java.util.List;

public interface Bridge {

    //System Functions:

    /**
     * System - Initialize trading System
     *
     * @return If succeed returns true. Otherwise,
     *         return false.
     */
    boolean initTradingSystem();

    /**
     * System - Shut down trading System
     *
     * @return If succeed returns true. Otherwise,
     *         return false.
     */
    boolean shutDownTradingSystem();

    /**
     * System - add admin to system
     *
     * @param adminId admin ID of the admin that make this request
     * @param email admin email
     * @param password user password
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int addAdmin(int adminId, String email, String password);

    //User Function
    /**
     * User - user register to the system
     *
     * @param email user email
     * @param password user password
     * @param birthday user birthday date format DD/MM/YYYY
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int register(String email, String password, String birthday);

    /**
     * User - user login to the system
     *
     * @param email user email
     * @param password user password
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int login(String email, String password);

    LoginData loginAndGetData(String email, String password);

    /**
     * User - user logout from the system
     *
     * @param user user ID
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int logout(int user);


    /**
     * Store - user create store
     *
     * @param user user ID
     * @param description store description
     * @return If succeed returns Store ID > 0. Otherwise,
     *         return -1.
     */
    int createStore(int user, String name, String description, String img);

    /**
     * System external supplier service management - getting all possible external service
     *
     * @param admin admin Id
     * @return If succeed returns list of possible supplier service. Otherwise,
     *         return null.
     */
    List<String> getPossibleExternalSupplierService(int admin);

    /**
     * System external supplier service management - getting all possible external service
     *
     * @return If succeed returns list of available supplier service. Otherwise,
     *         return null.
     */
    List<String> getAvailableExternalSupplierService();

    /**
     * System external supplier service management - adding external service
     *
     * @param admin      admin Id
     * @param esSupplier external payment service
     * @return If succeed returns `true`. Otherwise,
     * return `false`.
     */
    boolean addExternalSupplierService(int admin, String esSupplier);

    /**
     * System external supplier service management - adding external service
     *
     * @param adminId admin Id
     * @param esSupplier external supplier service name.
     * @param supplierAdapter external supplier service name.
     * @return If succeed returns `true`. Otherwise,
     *         return `false`.
     */
    boolean addExternalSupplierService(int adminId, String esSupplier, SupplierAdapter supplierAdapter);

    /**
     * System external supplier service management - remove external service
     *
     * @param admin admin Id
     * @param es external service
     * @return If succeed returns `true`. Otherwise,
     *         return `false`.
     */
    boolean removeExternalSupplierService(int admin, String es);

    /**
     * System external supplier service management - replace external service
     *
     * @param admin      admin Id
     * @param esSupplier external payment service
     * @return If succeed returns true. Otherwise,
     * return false.
     */
    boolean replaceExternalSupplierService(int admin, String esSupplier);

    /**
     * System external payment service management - getting all possible external service
     *
     * @param admin admin Id
     * @return If succeed returns list of possible payment service. Otherwise,
     *         return null.
     */
    List<String> getPossibleExternalPaymentService(int admin);

    /**
     * System external payment service management - getting all available external service
     *
     * @return If succeed returns list of available payment service. Otherwise,
     *         return null.
     */
    List<String> getAvailableExternalPaymentService();

    /**
     * System external payment service management - adding external service
     *
     * @param admin     admin Id
     * @param esPayment external payment service
     * @return If succeed returns 1. Otherwise,
     * return -1.
     */
    boolean addExternalPaymentService(int admin, String esPayment);

    /**
     * System external payment service management - adding external service
     *
     * @param adminId admin Id
     * @param esPayment external payment service name.
     * @param paymentAdapter external payment service adapter class.
     * @return If succeed returns true. Otherwise,
     *         return false.
     */
    boolean addExternalPaymentService(int adminId, String esPayment, PaymentAdapter paymentAdapter);

    /**
     * System external payment service management - remove external service
     *
     * @param admin admin Id
     * @param es external service
     * @return If succeed returns `true`. Otherwise,
     *         return `false`.
     */
    boolean removeExternalPaymentService(int admin, String es);

    /**
     * System external payment service management - replace external service
     *
     * @param admin admin Id
     * @param esPayment external payment service
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int replaceExternalPaymentService(int admin, String esPayment);

    /**
     * System external payment service management - getting all possible external service
     *
     * @param adminId admin id.
     * @return If succeed returns list of available payment service. Otherwise,
     *         return null.
     */
    List<String> getPaymentServicesPossibleOptions(int adminId);

    /**
     * Get user unread messages
     *
     * @param user username
     * @return If succeed return the list of messages of user. Otherwise, return null.
     */
    List<MessageInfo> getUnreadMessages(String user);

    /**
     * Get user all messages
     *
     * @param user user id
     * @return If succeed return the list of messages of user. Otherwise, return null.
     */
    List<MessageInfo> getAllMessages(int user);

    /**
     * Inventory management - adding product
     *
     * @param userId user id
     * @param storeId store id
     * @param categories list of the categories of product
     * @param name product name
     * @param description product description
     * @param price product price
     * @param quantity product quantity
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
     int addProduct(int userId, int storeId, List<String> categories, String name, String description, int price,
                          int quantity, String img);
    /**
     * Inventory management - remove product
     *
     * @param user user id
     * @param store store id
     * @param product product id.
     * @return If succeed returns true. Otherwise,
     *         return false.
     */
    boolean removeProduct(int user, int store, int product);

    /**
     * Inventory management - update product
     *
     * @param userId user id
     * @param storeId store id
     * @param productId product id
     * @param categories list of the categories of product
     * @param name product name
     * @param description product description
     * @param price product price
     * @param quantity product quantity
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int updateProduct(int userId, int storeId, int productId, List<String> categories, String name, String description, int price, int quantity);

    /**
     * Get products in the store
     *
     * @param store store id
     * @return If succeed return the list of products in the store. Otherwise, return empty
     *         list.
     */
    List<ProductInfo> getProductsInStore(int store);

    /**
     * Changing the types and rules (policy) of the store's discount
     *
     * @param userId user username
     * @param storeId store id
     * @param policy the policy.
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int changeDiscountPolicy(int userId,int storeId,String policy);

    /**
     * Changing the types and rules (policy) of the store's purchase
     *
     * @param userId user id
     * @param storeId store id
     * @param policy the policy.
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int changePurchasePolicy(int userId,int storeId,String policy);

    /**
     * Appointment new owner in the store
     *
     * @param user user id
     * @param store store id
     * @param newOwner id of the user who we want to appoint as an owner in store.
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int appointmentOwnerInStore(int user, int store,String newOwner);

    /**
     * Appointment new manger in the store
     *
     * @param user user id
     * @param store store id
     * @param manager id of the user who we want to appoint as a manager in store.
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int appointmentMangerInStore(int user, int store,String manager);

    /**
     * Close the store
     *
     * @param user user id
     * @param store store id
     * @return If succeed returns true. Otherwise,
     *         return false.
     */
    boolean closeStore(int user, int store);

    /**
     * Reopen the store
     *
     * @param user user id
     * @param store store id
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int reopenStore(int user, int store);

    /**
     * Gets information about positions in the store
     *
     * @param user user id
     * @param store store id
     * @return If succeed returns the list of positions in the store. Otherwise, return null.
     */
    List<PositionInfo> getPositionInStore(int user, int store);

    /**
     * Add the store manager permissions
     *
     * @param user user username
     * @param store store id
     * @param managerId manager id
     * @param permissionsId the permission id
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int addStoreManagerPermissions(int user, int store, int managerId, int permissionsId);

    /**
     * Remove the store manager permissions
     *
     * @param user user username
     * @param store store id
     * @param managerId manager id
     * @param permissionsIds  permission id
     * @return If succeed returns true. Otherwise,
     *         return false.
     */
    boolean removeStoreManagerPermissions(int user, int store, int managerId, List<Integer> permissionsIds);

    /**
     * Change the store manager permissions
     *
     * @param user user username
     * @param store store id
     * @param managerId manager id
     * @param permissionsIds list of the permissions
     * @return If succeed returns true. Otherwise,
     *         return false.
     */
    boolean addStoreManagerPermissions(int user, int store, int managerId, List<Integer> permissionsIds);

    /**
     * Gets information about store manager’s permissions
     *
     * @param user    user id
     * @param store   store id
     * @param manager manager id
     * @return If succeed returns the list of manger permissions in the store. Otherwise, return null.
     */
    PermissionInfo getManagerPermissionInStore(int user, int store, int manager);

    /**
     * Gets purchases history in store
     *
     * @param user user id
     * @param store store id
     * @return If succeed returns the list of purchases history in the store. Otherwise, return null.
     */
    List<PurchaseInfo> getStorePurchasesHistory(int user, int store);



    /**
     * Gets purchases history of buyer
     *
     * @param user user id
     * @param buyer buyer id
     * @return If succeed returns the list of purchases history of buyer. Otherwise, null.
     */
    List<PurchaseInfo> getBuyerPurchasesHistory(int user, int buyer);

    /**
     * Gets all admins
     *
     * @param adminId admin id
     * @return If succeed returns the list of admins. Otherwise, null.
     */
    List<AdminInfo> getAllAdmins(int adminId);

    /**
     * Gets all stores
     *
     * @return If succeed returns the list of stores. Otherwise, null.
     */
    List<StoreInfo> getAllStores();

    /**
     * Admin remove himself
     *
     * @param adminId admin id
     * @return If succeed returns 1. Otherwise,
     *          return -1.
     */
    int removeAdmin(int adminId);

    /**
     * Get store
     *
     * @param storeId store id
     * @return If succeed returns the asked store. Otherwise,
     *          return null.
     */
    StoreInfo getStore(int storeId);

    /**
     * Get product in store
     *
     * @param storeId store id
     * @return If succeed returns the list of products in the store. Otherwise,
     *          return null.
     */
    List<ProductInfo> getProductInStore(int storeId);


    /**
     * Guest - enter system
     *
     * @return If succeed returns guest id. Otherwise,
     *         return -1.
     */
    int enterSystem();

    /**
     * Guest/ Member - add product to cart
     *
     * @param user user id
     * @param store store id
     * @param productID product id
     * @param quantity product quantity
     * @return If succeed returns 1. Otherwise,
     *         return -1.
     */
    int addProductToCart(int user, int store, int productID, int quantity);

    /**
     * Guest/ Member - Get cart
     *
     * @param user user id
     * @return If succeed returns cart information . Otherwise,
     *          return null.
     */
    CartInfo getCart(int user);

    /**
     * Guest/ Member - Make Purchase
     *
     * @param user user id
     * @param payment payment details.
     * @param supplier payment details.
     * @return If succeed returns 1 . Otherwise,
     *          return -1.
     */
    int makePurchase(int user, JSONObject payment, JSONObject supplier);

    /**
     * Guest - exit system
     *
     * @param guestId guest id
     * @return If succeed returns guest id. Otherwise,
     *         return -1.
     */
    int exitSystem(int guestId);

    /**
     * Guest/ Member - remove product from cart
     *
     * @param userId user id
     * @param storeId store id
     * @param productId product id
     * @return If succeed returns guest id. Otherwise,
     *         return -1.
     */
    int removeProductFromCart(int userId, int storeId, int productId);

    /**
     * Guest/ Member - update product in the cart
     *
     * @param userId user id
     * @param storeId store id
     * @param productId product id
     * @return If succeed returns guest id. Otherwise,
     *         return -1.
     */
    int changeQuantityInCart(int userId, int storeId, int productId, int change);


    /**
     * Get notifications of the user
     *
     * @param userId user id
     * @return If succeed returns notification list. Otherwise,
     *         return null.
     */
    List<Notification> getNotifications(int userId);

    /**
     * Guest/ Member - get store information
     *
     * @param storeId store id
     * @return If succeed return store. Otherwise,
     *         return null.
     */
    StoreInfo getStoreInfo(int storeId);

    /**
     * Member - get role of the user in store
     *
     * @param userId user id
     * @param storeId store id
     * @return If succeed return role in store. Otherwise,
     *         return null.
     **/
    Role getRoleInStore(int userId, int workerId, int storeId);


}
