package junit;

import data.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class StoreOwnerTest extends ProjectTest{
    private ProductInfo goodProduct0;
    private ProductInfo goodProduct1;
    private ProductInfo goodProduct2;
    private ProductInfo goodProduct3;
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
     * 1. Inventory management: ADD/ Remove/ Update - V & TODO Alert Test?
     * 2. Change discount policy - V & TODO Alert Test?
     * 3. Change purchase policy - V & TODO Alert Test?
     * 4. Appointment Store Owner - V & TODO Alert Test
     * 5. Appointment Store Manager - V & TODO Alert Test
     * 6. Change Store Manager permissions - V & Short TODO Alert
     * 7. Close Store - V & Short TODO: Alert
     * 8. Ask for getting data about position in the store - V
     * 9. get information about purchase in the store - V & TODO: add the todos things
     * 10. Ask for getting data about store manager permissions in the store - V
     * 11. get information about purchase of buyer - V & TODO: add the todos things
     */

    //Get information about purchase of buyer:

    @Test
    public void testGetBuyerPurchasesHistory(){
        int adminId = this.mainAdmin.getAdminId();
        int buyer = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        List<PurchaseInfo> purchases = this.getBuyerPurchasesHistory(adminId, buyer);
        assertNotNull(purchases);
        assertTrue(purchases.size() == 0);
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
        int adminId = this.mainAdmin.getAdminId();
        int buyer = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        List<PurchaseInfo> purchases = this.getBuyerPurchasesHistory(adminId, buyer);
        assertNotNull(purchases);
        assertTrue(purchases.size() == 0);
        //TODO: add processes of purchase of product
        purchases = this.getBuyerPurchasesHistory(adminId, buyer);
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
        int status = this.appointmentMangerInStore(uid.getUserId(), storeId,uIdManager);
        assertTrue(status > 0);
        List<PermissionInfo> permissions = this.getManagerPermissionInStore(uid.getUserId(), storeId, uIdManager);
        assertNotNull(permissions);
        assertTrue(permissions.size() > 0);
    }

    @Test
    public void testGetManagerAnotherStorePermissionInStore(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int anotherStoreId = stores.get(1).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentMangerInStore(uid, anotherStoreId,uIdManager);
        assertTrue(status > 0);
        List<PermissionInfo> permissions = this.getManagerPermissionInStore(uid, storeId, uIdManager);
        assertNull(permissions);
    }


    @Test
    public void testGetManagerPermissionInStoreAfterChange(){
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        int status = this.appointmentMangerInStore(ui.getUserId(), storeId,uIdManager);
        List<Integer> permissions2change = new ArrayList<>();
        permissions2change.add(0);
        assertTrue(status > 0);
        List<PermissionInfo> permissions = this.getManagerPermissionInStore(ui.getUserId(), storeId, uIdManager);
        assertNotNull(permissions);
        assertTrue(permissions.size() > 0);
        status = this.changeStoreManagerPermissions(ui.getUserId(), storeId,uIdManager, permissions2change);
        assertTrue(status > 0);
        permissions = this.getManagerPermissionInStore(ui.getUserId(), storeId, uIdManager);
        assertNotNull(permissions);
        assertTrue(permissions.size() > 0);
    }

    @Test
    public void testGetUnexistManagerPermissionInStore(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        List<PermissionInfo> permissions = this.getManagerPermissionInStore(uid, storeId, ERROR);
        assertNull(permissions);
    }

    @Test
    public void testNotOwnerTryGetManagerPermissionInStore(){
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        List<PermissionInfo> permissions = this.getManagerPermissionInStore(uid, storeId, ERROR);
        assertNull(permissions);
    }

    //Get information about purchase in the store:

    @Test
    public void testGetStorePurchasesHistory(){
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
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
        //TODO: add procces of purchase of product
        //Add product to cart
        int status = addProductToCart(uid.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        status  = makePurchase(uid.getUserId(), "00000000000");
        assertTrue(status > 0);
        purchases = this.getStorePurchasesHistory(uid.getUserId(), storeId);
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
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        List<PositionInfo> positions = this.getPositionInStore(uid, storeId);
        assertNotNull(positions);
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentOwnerInStore(uid, storeId,uIdOwner);
        assertTrue(status > 0);
        positions = this.getPositionInStore(uid, storeId);
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
    public void testReopenStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
        status = this.reopenStore(uid, storeId);
        assertTrue(status > 0);
    }

    @Test
    public void testAlertReopenStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
        status = this.reopenStore(uid, storeId);
        assertTrue(status > 0);
        //TODO: Get Alert
    }

    @Test
    public void testReopenStoreButStoreAlreadyOpen() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.reopenStore(uid, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testNotCreatorTryReopenStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
        status = this.reopenStore(-1, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testOwnerTryReopenStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentOwnerInStore(uid, storeId,uIdOwner);
        assertTrue(status > 0);
        status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
        status = this.reopenStore(uIdOwner, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testManagerTryReopenStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentMangerInStore(uid, storeId,uIdManager);
        assertTrue(status > 0);
        status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
        status = this.reopenStore(uIdManager, storeId);
        assertTrue(status < 0);
    }

    //Close Store:

    @Test
    public void testCloseStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
    }

    @Test
    public void testAlertCloseStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
        //TODO: Get Alert
    }

    @Test
    public void testCloseStoreButStoreAlreadyClose() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.closeStore(uid, storeId);
        assertTrue(status > 0);
        status = this.closeStore(uid, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testNotCreatorTryCloseStore() {
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.closeStore(ERROR, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testOwnerTryCloseStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentOwnerInStore(uid, storeId,uIdOwner);
        assertTrue(status > 0);
        status = this.closeStore(uIdOwner, storeId);
        assertTrue(status < 0);
    }

    @Test
    public void testManagerTryCloseStore() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentMangerInStore(uid, storeId,uIdManager);
        assertTrue(status > 0);
        status = this.closeStore(uIdManager, storeId);
        assertTrue(status < 0);
    }

    //Change Store Manager permissions :

    @Test
    public void testChangeStoreManagerPermissions() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.appointmentMangerInStore(uid, storeId,uIdManager);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        status = this.changeStoreManagerPermissions(uid, storeId, uIdManager, permissions);
        assertTrue(status > 0);
    }

    @Test
    public void testAlertChangeStoreManagerPermissions() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.appointmentMangerInStore(uid, storeId,uIdManager);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        status = this.changeStoreManagerPermissions(uid, storeId, uIdManager, permissions);
        assertTrue(status > 0);
        //TODO: Add functionality of getting message
    }

    @Test
    public void testChangeStoreManagerUnRealPermissions() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.appointmentMangerInStore(uid, storeId,uIdManager);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(ERROR);
        permissions.add(1);
        permissions.add(2);
        status = this.changeStoreManagerPermissions(uid, storeId, uIdManager, permissions);
        assertTrue(status < 0);
    }

    @Test
    public void testChangeStoreManagerPermissionsOnSomeoneElse() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        int status = this.changeStoreManagerPermissions(uid, storeId, uIdManager, permissions);
        assertTrue(status < 0);
    }

    @Test
    public void testChangeStoreManagerPermissionsOnOwner() {
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        int status = this.appointmentOwnerInStore(uid, storeId,uIdOwner);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        status = this.changeStoreManagerPermissions(uid, storeId, uIdOwner, permissions);
        assertTrue(status < 0);
    }

    @Test
    public void testNotOwnerTryChangeStoreManagerPermissions() {
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int notOwnerId = this.users_dict.get(users[2][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        int status = this.appointmentMangerInStore(ui.getUserId(), storeId,uIdManager);
        assertTrue(status > 0);
        List<Integer> permissions = new ArrayList<>();
        permissions.add(0);
        permissions.add(1);
        permissions.add(2);
        status = this.changeStoreManagerPermissions(notOwnerId, storeId, uIdManager, permissions);
        assertTrue(status < 0);
    }

    //Appointment Store Manager:

    @Test
    public void testAppointmentManagerInStore() {
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        int status = this.appointmentMangerInStore(ui.getUserId(), storeId,uIdManager);
        assertTrue(status > 0);
    }

    @Test
    public void testAppointmentUnRealManagerInStore() {
        UserInfo ui = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        int status = this.appointmentMangerInStore(ui.getUserId(), storeId, ERROR);
        assertFalse(status > 0);
    }

    @Test
    public void testAppointmentExistOwnerToMangerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId, uIdOwner);
        assertTrue(status > 0);
        status = this.appointmentMangerInStore(uid.getUserId(), storeId, uIdOwner);
        assertFalse(status > 0);
    }

    @Test
    public void testAppointmentExistManagerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int status = this.appointmentMangerInStore(uid.getUserId(), storeId,uIdManager);
        assertTrue(status > 0);
        status = this.appointmentMangerInStore(uid.getUserId(), storeId,uIdManager);
        assertTrue(status < 0);
    }

    @Test
    public void testNotOwnerTryAppointmentManagerInStore() {
        UserInfo uid = this.users_dict.get(users[2][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentMangerInStore(uid.getUserId(), storeId,uIdManager);
        assertTrue(status < 0);
    }

    //Appointment Store Owner:

    @Test
    public void testAppointmentOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int storeId = stores.get(0).getStoreId();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,uIdOwner);
        assertTrue(status > 0);
    }

    @Test
    public void testAppointmentExistMangerToOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdManager = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int status = this.appointmentMangerInStore(uid.getUserId(), storeId,uIdManager);
        assertTrue(status > 0);
        status = this.appointmentOwnerInStore(uid.getUserId(), storeId,uIdManager);
        assertTrue(status > 0);
    }

    @Test
    public void testAppointmentExistOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,uIdOwner);
        assertTrue(status > 0);
        status = this.appointmentOwnerInStore(uid.getUserId(), storeId,uIdOwner);
        assertTrue(status < 0);
    }

    @Test
    public void testAppointmentUnRealOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,ERROR);
        assertTrue(status < 0);
    }

    @Test
    public void testNotOwnerTryAppointmentOwnerInStore() {
        UserInfo uid = this.users_dict.get(users[2][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        int uIdOwner = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        int status = this.appointmentOwnerInStore(uid.getUserId(), storeId,uIdOwner);
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
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 > 0);
        product2Add = goodProduct1;
        productId1 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId1 > 0);

        assertTrue(productId0 != productId1, "Different product must have different ids !");
    }

    @Test
    public void testNotOwnerTryAddProduct() {

        int productId0, productId1;
        int uid = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 < 0);
        product2Add = goodProduct1;
        productId1 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId1 < 0);
    }

    @Test
    public void testAddMissDetailsProduct() {
        int productId0, productId1;
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        ProductInfo product2Add = goodProduct1;
        productId0 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 < 0);
        product2Add = goodProduct2;
        productId1 = this.addProduct(uid, stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId1 < 0);

    }

    @Test
    public void testAddExistsProduct() {
        int productId0, productId1;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        ProductInfo product2Add = goodProduct0;
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        productId0 = this.addProduct(uid.getUserId(), stores.get(1).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 > 0);
        productId1 = this.addProduct(uid.getUserId(), stores.get(1).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId1 < 0);
    }

    @Test
    public void testRemoveProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(0).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 >= 0);
        status = this.removeProduct(uid.getUserId(), stores.get(0).getStoreId(), productId0);
        assertTrue(status > 0);
    }

    @Test
    public void testNotOwnerTryRemoveProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(2).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 >= 0);
        int ui = this.users_dict.get(users[1][USER_EMAIL]).getUserId();
        status = this.removeProduct(ui, stores.get(2).getStoreId(), productId0);
        assertTrue(status < 0);
    }

    @Test
    public void testRemoveUnexistsProduct() {
        int productId0 = 1, status;
        int uid = this.users_dict.get(users[0][USER_EMAIL]).getUserId();
        status = this.removeProduct(uid, stores.get(3).getStoreId(), productId0);
        assertTrue(status < 0);
    }

    @Test
    public void testUpdateProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(2).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 >= 0);
        product2Add.setPrice(60);
        status = this.updateProduct(uid.getUserId(), stores.get(2).getStoreId(), productId0, product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(status > 0);
    }

    @Test
    public void testNotOwnerTryUpdateProduct() {
        int productId0, status;
        UserInfo uid = this.users_dict.get(users[0][USER_EMAIL]);
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        ProductInfo product2Add = goodProduct0;
        productId0 = this.addProduct(uid.getUserId(), stores.get(2).getStoreId(), product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 > 0);
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
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(productId0 > 0);
        product2Add.setPrice(ERROR);
        status = this.updateProduct(ui.getUserId(), stores.get(2).getStoreId(), productId0, product2Add.getCategories(), product2Add.getName(), product2Add.getDescription()
                , product2Add.getPrice(), product2Add.getQuantity());
        assertTrue(status < 0);
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

    @Test
    public void testAddProductCartAndRemoveAtSameTime(){
        GuestInfo buyer1 = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer1.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(removeProduct(uid.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId()));
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
        appointmentOwnerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid1.getUserId());
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(appointmentOwnerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid2.getUserId()));
        });
        Thread t2 = new Thread(() -> {
            i.addAndGet(appointmentOwnerInStore(uid1.getUserId(), stores.get(0).getStoreId(), uid2.getUserId()));
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
        appointmentOwnerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid1.getUserId());
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(appointmentMangerInStore(creator.getUserId(), stores.get(0).getStoreId(), uid2.getUserId()));
        });
        Thread t2 = new Thread(() -> {
            i.addAndGet(appointmentMangerInStore(uid1.getUserId(), stores.get(0).getStoreId(), uid2.getUserId()));
        });
        t1.start();
        t2.start();
        assertTrue(i.get() <= 0);

    }
}
