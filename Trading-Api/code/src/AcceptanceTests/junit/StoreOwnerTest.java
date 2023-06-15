package junit;

import data.*;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.messageRelated.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class StoreOwnerTest extends ProjectTest{
    private ProductInfo goodProduct0;
    private ProductInfo goodProduct1;
    private ProductInfo goodProduct2;
    private ProductInfo goodProduct3;

    private JSONObject payment = createPaymentJson();
    private JSONObject supplier = createSupplierJson();
    private static final int ERROR = -1;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.goodProduct0 = createProduct0();
        this.goodProduct1 = createProduct1();
        this.goodProduct2 = createProduct2();
        this.goodProduct3 = createProduct3();
    }

    @AfterEach
    public void tearDown() {
        this.goodProduct0 = null;
        this.goodProduct1 = null;
        this.goodProduct2 = null;
        this.goodProduct3 = null;
    }



    /** TODO: Add every test also login and logout
     * 1. Inventory management: ADD/ Remove/ Update - V
     * 2. Change discount policy - V & TODO Alert Test?
     * 3. Change purchase policy - V & TODO Alert Test?
     * 4. Appointment Store Owner - V
     * 5. Appointment Store Manager - V
     * 6. Change Store Manager permissions - V
     * 7. Close Store - V
     * 8. Ask for getting data about position in the store - V
     * 9. get information about purchase in the store - V & TODO: add the todos things
     * 10. Ask for getting data about store manager permissions in the store - V
     * 11. get information about purchase of buyer - V & TODO: add the todos things
     */

    @Test
    private void isGoodLogin(UserInfo ui)
    {
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        assertTrue(ui.getUserId() > 0);
    }

    @Test
    private LoginData isGoodLoginWithData(UserInfo ui)
    {
        LoginData data = loginAndGetData(ui.getEmail(), ui.getPassword());
        assertNotNull(data);
        ui.setUserId(data.getUserId());
        assertTrue(ui.getUserId() > 0);
        return data;
    }

    @Test
    private void isGoodLogout(UserInfo ui)
    {
        assertTrue(logout(ui.getUserId()) > 0);
    }

    private void clearNotification(UserInfo ui)
    {
        int pre = 0;
        getNotifications(ui.getUserId());
        pre = this.getNotifications(ui.getUserId()).size();
        assertEquals(0, pre);
    }

    private void except2Notification(UserInfo ui, String notificationMsg)
    {
        List<Notification> notifications = this.getNotifications(ui.getUserId());
        assertEquals(1, notifications.size());
        String n1 = notifications.get(0).toString();
        assertTrue(n1.contains(notificationMsg));
    }

    @Test
    private void except2NotificationAtLogin(UserInfo ui, String notificationMsg)
    {
        LoginData data = isGoodLoginWithData(ui);
        List<String> notifications = data.getNotifications();
        assertEquals(1, notifications.size());
        assertTrue(notifications.get(0).contains(notificationMsg));
    }

    //Get information about purchase of buyer:

    @Test
    public void testGetBuyerPurchasesHistory(){
        int adminId = this.mainAdmin.getAdminId();
        int buyer = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = adminLogin(this.mainAdmin.getEmail(), this.mainAdmin.getPassword());
        List<PurchaseInfo> purchases = this.getBuyerPurchasesHistory(adminId, buyer);
        //assertNotNull(purchases);
        assertEquals(0, purchases.size());
    }

    @Test
    public void testNotAdminTryGetBuyerPurchasesHistory(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int buyer = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        List<PurchaseInfo> purchases = this.getBuyerPurchasesHistory(uid, buyer);
        assertNull(purchases);
    }

    @Test
    public void testAfterPurchaseGetBuyerPurchasesHistory(){
        AdminInfo admin = this.mainAdmin;
        UserInfo buyer = this.users_dict.get(users[1][USER_EMAIL]);
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        List<PurchaseInfo> purchases = this.getBuyerPurchasesHistory(admin.getAdminId(), buyer.getUserId());
        assertNotNull(purchases);
        assertEquals(0, purchases.size());
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        status  = makePurchase(buyer.getUserId(), payment, supplier);
        assertTrue(status > 0);
        purchases = this.getBuyerPurchasesHistory(admin.getAdminId(), buyer.getUserId());
        assertNotNull(purchases);
        assertTrue(purchases.size() > 0);
    }


    //Ask for getting data about store manager permissions in the store:

    @Test
    public void testGetManagerPermissionInStore(){
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status > 0);
        PermissionInfo permissions = this.getManagerPermissionInStore(uid.getUserId(), storeId, uIdManager);
        assertNotNull(permissions);
        assertTrue(permissions.size() > 0);
    }

    @Test
    public void testGetManagerAnotherStorePermissionInStore(){
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        int anotherStoreId = stores.get(1).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), anotherStoreId,managerEmail);
        assertTrue(status > 0);
        PermissionInfo permissions = this.getManagerPermissionInStore(uid.getUserId(), storeId, uIdManager);
        assertNull(permissions);
    }


    @Test
    public void testGetManagerPermissionInStoreAfterChange(){
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(ui.getUserId(), storeId,managerEmail);
        List<Integer> permissions2change = new ArrayList<>();
        permissions2change.add(0);
        assertTrue(status > 0);
        PermissionInfo permissions = this.getManagerPermissionInStore(ui.getUserId(), uIdManager, storeId);
        assertNotNull(permissions);
        assertTrue(permissions.size() > 0);
        assertTrue(this.addStoreManagerPermissions(ui.getUserId(), storeId, uIdManager, permissions2change));
        permissions = this.getManagerPermissionInStore(ui.getUserId(), uIdManager, storeId);
        assertNotNull(permissions);
        assertTrue(permissions.size() > 0);
    }

    @Test
    public void testGetUnexistManagerPermissionInStore(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        PermissionInfo permissions = this.getManagerPermissionInStore(uid, storeId, ERROR);
        assertNull(permissions);
    }

    @Test
    public void testNotOwnerTryGetManagerPermissionInStore(){
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        PermissionInfo permissions = this.getManagerPermissionInStore(uid, storeId, ERROR);
        assertNull(permissions);
    }

    //Get information about purchase in the store:

    @Test
    public void testGetStorePurchasesHistory(){
        int uid = login(users[0][USER_EMAIL], users[0][USER_PASS]);
        int storeId = stores.get(0).getStoreId();
        List<PurchaseInfo> purchases = this.getStorePurchasesHistory(uid, storeId);
        assertNotNull(purchases);
        assertTrue(purchases.size() == 0);
    }

    @Test
    public void testNotAdminOrOwnerTryGetStorePurchasesHistory(){
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        List<PurchaseInfo> purchases = this.getStorePurchasesHistory(uid, storeId);
        assertNull(purchases);
    }

    @Test
    public void testAdminGetStorePurchasesHistory(){
        int adminId = this.mainAdmin.getAdminId();
        int storeId = stores.get(0).getStoreId();
        List<PurchaseInfo> purchases = this.getStorePurchasesHistory(adminId, storeId);
        assertNotNull(purchases);
        assertTrue(purchases.size() == 0);
    }

    @Test
    public void testAfterPurchaseGetStorePurchasesHistory(){
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        List<PurchaseInfo> purchases = this.getStorePurchasesHistory(uid.getUserId(), storeId);
        assertNotNull(purchases);
        assertTrue(purchases.size() == 0);
        //Add product to cart
        int status = addProductToCart(uid.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        status  = makePurchase(uid.getUserId(), payment, supplier);
        assertTrue(status > 0);
        UserInfo creator = this.users_dict.get(users[1][USER_EMAIL]);
        creator.setUserId(login(creator.getEmail(), creator.getPassword()));
        purchases = this.getStorePurchasesHistory(creator.getUserId(), stores.get(4).getStoreId());
        assertNotNull(purchases);
        assertTrue(purchases.size() > 0);
    }


    //Ask for getting data about position in the store:

    @Test
    public void testGetPositionInStore(){
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        List<PositionInfo> positions = this.getPositionInStore(ui.getUserId(), storeId);
        assertNotNull(positions);
    }

    @Test
    public void testGetPositionsInStore(){
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        List<PositionInfo> positions = this.getPositionInStore(uid.getUserId(), storeId);
        assertNotNull(positions);
        String uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,uIdOwner);
        assertTrue(status > 0);
        positions = this.getPositionInStore(uid.getUserId(), storeId);
        assertNotNull(positions);
        assertTrue(positions.size() > 1);
    }

    @Test
    public void testNotOwnerTryGetPositionInStore(){
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        List<PositionInfo> positions = this.getPositionInStore(uid, storeId);
        assertNull(positions);
    }

    //Reopen Store:
    @Test
    private void goodCloseStore(int userId, int storeId)
    {
        assertTrue(closeStore(userId, storeId));
    }
    @Test
    public void testReopenStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        goodCloseStore(uid.getUserId(), storeId);
        int status = this.reopenStore(uid.getUserId(), storeId);
        assertTrue(status > 0);
    }

    @Test
    public void testAlertReopenStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        goodCloseStore(uid.getUserId(), storeId);
        clearNotification(uid);
        int status = this.reopenStore(uid.getUserId(), storeId);
        assertTrue(status > 0);
        except2Notification(uid, "the store: " + storeId + " has been reOpened");
    }

    @Test
    public void testReopenStoreButStoreAlreadyOpen() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        int status = this.reopenStore(uid.getUserId(), storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testNotCreatorTryReopenStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        goodCloseStore(uid.getUserId(),storeId);
        int status = this.reopenStore(-1, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testOwnerTryReopenStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        String ownerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,ownerEmail);
        assertTrue(status > 0);
        goodCloseStore(uid.getUserId(), storeId);
        status = this.reopenStore(uIdOwner, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testManagerTryReopenStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status > 0);
        goodCloseStore(uid.getUserId(), storeId);
        status = this.reopenStore(uIdManager, storeId);
        assertTrue(status < 0);
    }

    //Close Store:

    @Test
    public void testCloseStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        goodCloseStore(uid.getUserId(), storeId);
    }

    @Test
    public void testAlertCloseStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        clearNotification(uid);
        goodCloseStore(uid.getUserId(), storeId);
        except2Notification(uid, "the store: " + storeId + " has been temporarily closed");
    }

    @Test
    private void badCloseStore(int userId, int storeId)
    {
        assertFalse(closeStore(userId, storeId));
    }

    @Test
    public void testCloseStoreButStoreAlreadyClose() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        goodCloseStore(uid.getUserId(), storeId);
        badCloseStore(uid.getUserId(), storeId);
    }

    @Test
    public void testNotCreatorTryCloseStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        badCloseStore(ERROR, storeId);
    }

    @Test
    public void testOwnerTryCloseStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        UserInfo uIdOwner = this.users_dict.get(users[1][USER_EMAIL]);
        uIdOwner.setUserId(login(uIdOwner.getEmail(), uIdOwner.getPassword()));
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,uIdOwner.getEmail());
        assertTrue(status > 0);
        badCloseStore(uIdOwner.getUserId(), storeId);
    }

    @Test
    public void testManagerTryCloseStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        UserInfo uIdManager = this.users_dict.get(users[1][USER_EMAIL]);
        uIdManager.setUserId(login(uIdManager.getEmail(), uIdManager.getPassword()));
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,uIdManager.getEmail());
        assertTrue(status > 0);
        badCloseStore(uIdManager.getUserId(), storeId);
    }

    //Change Store Manager permissions :

    @Test
    public void testChangeStoreManagerPermissions() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        UserInfo uIdManager = this.users_dict.get(users[1][USER_EMAIL]);
        uIdManager.setUserId(login(uIdManager.getEmail(), uIdManager.getPassword()));
        int storeId = stores.get(0).getStoreId();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,uIdManager.getEmail());
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        assertTrue(this.addStoreManagerPermissions(uid.getUserId(), storeId, uIdManager.getUserId(), permissions));
    }

    @Test
    public void testAlertChangeStoreManagerPermissions() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        assertTrue(this.addStoreManagerPermissions(uid.getUserId(), storeId, uIdManager, permissions));
    }

    @Test
    public void testChangeStoreManagerUnRealPermissions() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(ERROR);
        permissions.add(1);
        permissions.add(2);
        assertTrue(this.addStoreManagerPermissions(uid.getUserId(), storeId, uIdManager, permissions));
    }

//    @Test
//    public void testChangeStoreManagerPermissionsOnSomeoneElse() {
//        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
//        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
//        int storeId = stores.get(0).getStoreId();
//        List<Integer> permissions = new ArrayList<>();
//        permissions.add(0);
//        permissions.add(1);
//        permissions.add(2);
//        assertTrue(this.addStoreManagerPermissions(uid, storeId, uIdManager, permissions));
//    }

    @Test
    public void testChangeStoreManagerPermissionsOnOwner() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        String ownerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int storeId = stores.get(0).getStoreId();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,ownerEmail);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        assertTrue(this.addStoreManagerPermissions(uid.getUserId(), storeId, uIdOwner, permissions));
    }

    @Test
    public void testNotOwnerTryChangeStoreManagerPermissions() {
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int notOwnerId = this.users_dict.get(users[2][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(ui.getUserId(), storeId,managerEmail);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        assertFalse(this.addStoreManagerPermissions(notOwnerId, storeId, uIdManager, permissions));
    }

    //Appointment Store Manager:

    @Test
    public void testAppointmentNotLoginManagerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
    }

    @Test
    public void testAppointmentLoginManagerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointManager);
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
    }

    @Test
    public void testAlertAppointmentManagerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointManager);
        clearNotification(appointManager);
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
        except2Notification(appointManager, "you have been appointed to manager in store: " + storeId);
    }

    @Test
    public void testAlertAppointmentLogoutManagerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointManager);
        clearNotification(appointManager);
        isGoodLogout(appointManager);
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
        except2NotificationAtLogin(appointManager, "you have been appointed to manager in store: " + storeId);
    }

    @Test
    public void testAppointmentUnRealManagerInStore() {
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        int status = this.appointmentManagerInStore(ui.getUserId(), storeId, null);
        assertFalse(status > 0);
    }

    @Test
    public void testAppointmentExistOwnerToOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        String ownerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId, ownerEmail);
        assertTrue(status > 0);
        status = this.appointmentOwnerInStore(uid.getUserId(), storeId, ownerEmail);
        assertFalse(status > 0);
    }

    @Test
    public void testAppointmentExistManagerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status > 0);
        status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status < 0);
    }

    @Test
    public void testNotOwnerTryAppointmentManagerInStore() {
        UserInfo uid = this.users_dict.get(users[2][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status < 0);
    }

    //Appointment Store Owner:

    @Test
    public void testAppointmentNotLoginOwnerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointOwner = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        int status = this.appointmentOwnerInStore(storeOwner.getUserId(), storeId, appointOwner.getEmail());
        assertTrue(status > 0);
    }

    @Test
    public void testAppointmentOwnerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointOwner = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointOwner);
        int status = this.appointmentOwnerInStore(storeOwner.getUserId(), storeId, appointOwner.getEmail());
        assertTrue(status > 0);
    }

    @Test
    public void testAppointmentAlertOwnerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointOwner = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointOwner);
        clearNotification(appointOwner);
        int status = this.appointmentOwnerInStore(storeOwner.getUserId(), storeId, appointOwner.getEmail());
        assertTrue(status > 0);
        except2Notification(appointOwner, "you have been appointed to owner in store: " + storeId);
    }

    @Test
    public void testAppointmentAlertLogoutOwnerInStore() {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointOwner = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointOwner);
        clearNotification(appointOwner);
        isGoodLogout(appointOwner);
        int status = this.appointmentOwnerInStore(storeOwner.getUserId(), storeId, appointOwner.getEmail());
        assertTrue(status > 0);
        except2NotificationAtLogin(appointOwner, "you have been appointed to owner in store: " + storeId);
    }

    @Test
    public void testAppointmentExistMangerToOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        String ownerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        String managerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentManagerInStore(uid.getUserId(), storeId,managerEmail);
        assertTrue(status > 0);
        status = this.appointmentOwnerInStore(uid.getUserId(), storeId,ownerEmail);
        assertTrue(status > 0);
    }

    @Test
    public void testAppointmentExistOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        String ownerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,ownerEmail);
        assertTrue(status > 0);
        status = this.appointmentOwnerInStore(uid.getUserId(), storeId,ownerEmail);
        assertTrue(status < 0);
    }

    @Test
    public void testAppointmentUnRealOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,null);
        assertTrue(status < 0);
    }

    @Test
    public void testNotOwnerTryAppointmentOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[2][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        String ownerEmail = this.users_dict.get(users[1][USER_EMAIL]).getEmail();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,ownerEmail);
        assertTrue(status < 0);
    }

    //Change purchase policy:

    @Test
    public void testChangePurchasePolicy() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int status = this.changePurchasePolicy(uid.getUserId(), storeId,"Bid");
        assertTrue(status > 0);
    }

    @Test
    public void testChangePurchaseUnRealPolicy() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.changePurchasePolicy(uid, storeId,"Shalom");
        assertTrue(status < 0);
    }

    @Test
    public void testNotOwnerTryChangePurchasePolicy() {
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.changePurchasePolicy(uid, storeId,"Bid");
        assertTrue(status < 0);
    }

    //Change discount policy:
    @Test
    public void testChangeDiscountPolicy() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.changeDiscountPolicy(uid, storeId,"50% every product");
        assertTrue(status > 0);
    }

    @Test
    public void testChangeDiscountUnRealPolicy() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.changeDiscountPolicy(uid, storeId,"Shalom");
        assertTrue(status < 0);
    }

    @Test
    public void testNotOwnerTryChangeDiscountPolicy() {
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.changeDiscountPolicy(uid, storeId,"50% every product");
        assertTrue(status < 0);
    }

    //Inventory management: ADD/ Remove/ Update:
    @Test
    public void testAddProduct() {
        int productId0, productId1;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 > -1);
        product2Add = goodProduct1;
        productId1 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId1 > 0);

        assertTrue(productId0 != productId1, "Different product must have different ids !");
    }

    @Test
    public void testNotOwnerTryAddProduct() {

        int productId0, productId1;
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 < 0);
        product2Add = goodProduct1;
        productId1 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId1 < 0);
    }

    @Test
    public void testAddMissDetailsProduct() {
        int productId0, productId1;
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        ProductInfo product2Add = goodProduct1;
        productId0 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 < 0);
        product2Add = goodProduct2;
        productId1 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId1 < 0);

    }

    @Test
    public void testAddExistsProduct() {
        int productId0, productId1;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        ProductInfo product2Add = goodProduct0;
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        productId0 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 > -1);
        productId1 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId1 < 0);
    }

    @Test
    public void testRemoveProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 >= 0);
        assertTrue(this.removeProduct(uid.getUserId(), stores.get(0).getStoreId(), productId0));
    }

    @Test
    public void testNotOwnerTryRemoveProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 >= 0);
        int ui = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        assertFalse(this.removeProduct(ui, stores.get(2).getStoreId(), productId0));
    }

    @Test
    public void testRemoveUnexistsProduct() {
        int productId0 = 1, status;
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        assertFalse(this.removeProduct(uid, stores.get(3).getStoreId(), productId0));
    }

    @Test
    public void testUpdateProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 >= 0);
        product2Add.setPrice(60);
        status = this.updateProduct(uid.getUserId(), stores.get(0).getStoreId(), productId0, product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(status > 0);
    }

    @Test
    public void testNotOwnerTryUpdateProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 > -1);
        int ui = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        product2Add.setPrice(60);
        status = this.updateProduct(ui, stores.get(2).getStoreId(), productId0, product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(status < 0);
    }

    @Test
    public void testUpdateMissDetailsProduct() {
        int productId0, status;
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(ui.getUserId(), stores.get(2).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity(), product2Add.getImg());
        assertTrue(productId0 > -1);
        product2Add.setPrice(ERROR);
        status = this.updateProduct(ui.getUserId(), stores.get(2).getStoreId(), productId0, product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(status > 0);
    }

    @Test
    public void testUpdateUnexistsProduct() {
        int productId0 = 1, status;
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        ProductInfo product2Add = goodProduct0;
        status = this.updateProduct(uid, stores.get(2).getStoreId(), productId0, product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(status < 0);
    }

    private int bool2int(boolean ans)
    {
        if (ans)
        {
            return 1;
        }
        return -1;
    }

    @Test
    public void testAddProductCartAndRemoveAtSameTime(){
        GuestInfo buyer1 = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer1.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(bool2int(removeProduct(uid.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId())));
        });
        Thread t2 = new Thread(() -> {
            i.addAndGet(addProductToCart(buyer1.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1));
        });
        t1.start();
        t2.start();
        assertTrue(i.get() <= 0);

    }

    @Test
    public void appointOwnerSameTime(){
        UserInfo creator = this.users_dict.get(users[0][USER_EMAIL]);//Owner of store 4
        UserInfo uid1 = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        UserInfo uid2 = this.users_dict.get(users[2][USER_EMAIL]);//Owner of store 4
        //Login
        uid1.setUserId(login(uid1.getEmail(), uid1.getPassword()));
        uid2.setUserId(login(uid2.getEmail(), uid2.getPassword()));
        appointmentOwnerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid1.getEmail());
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(appointmentOwnerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid2.getEmail()));
        });
        Thread t2 = new Thread(() -> {
            i.addAndGet(appointmentOwnerInStore(uid1.getUserId(), stores.get(0).getStoreId(), uid2.getEmail()));
        });
        t1.start();
        t2.start();
        assertTrue(i.get() <= 0);

    }

    @Test
    public void appointManagerSameTime(){
        UserInfo creator = this.users_dict.get(users[0][USER_EMAIL]);//Owner of store 4
        UserInfo uid1 = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        UserInfo uid2 = this.users_dict.get(users[2][USER_EMAIL]);//Owner of store 4
        //Login
        uid1.setUserId(login(uid1.getEmail(), uid1.getPassword()));
        uid2.setUserId(login(uid2.getEmail(), uid2.getPassword()));
        appointmentOwnerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid1.getEmail());
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(appointmentManagerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid2.getEmail()));
        });
        Thread t2 = new Thread(() -> {
            i.addAndGet(appointmentManagerInStore(uid1.getUserId(), stores.get(0).getStoreId(), uid2.getEmail()));
        });
        t1.start();
        t2.start();
        assertTrue(i.get() <= 0);

    }
}
