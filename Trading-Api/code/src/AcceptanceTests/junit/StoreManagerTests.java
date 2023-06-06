package junit;

import data.LoginData;
import data.ProductInfo;
import data.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.stateRelated.Role;

import static org.junit.jupiter.api.Assertions.*;

public class StoreManagerTests extends ProjectTest{

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    @AfterEach
    public void tearDown() {
        super.tearDown();
    }

    /**
     updateProduct, //updates product fields
     changeStoreDescription, // manager, owner, creator
     changePurchasePolicy, // manager, owner, creator
     changeDiscountPolicy, // manager, owner, creator
     addPurchaseConstraint, // manager, owner, creator
     addDiscountConstraint, // manager, owner, creator
     fireManager, // manager, owner, creator
     changeManagerPermission, // owner, creator
     checkWorkersStatus, // manager, owner, creator
     viewMessages, // manager, owner, creator
     answerMessage, // manager, owner, creator
     seeStoreHistory, // manager, owner, creator
     seeStoreOrders, // manager,owner,creator
     */
    /**
     * Store Manager Permissions:
     * 1. Appoint Manager
     * 2. Add Product
     * 3.
     *
     **/

    @Test
    private void isGoodLogin(UserInfo ui)
    {
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        assertTrue(ui.getUserId() > 0);
    }

    @Test
    private void isGoodManagerAppoint(UserInfo storeOwner, UserInfo appointManager, int storeId)
    {
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
        Role role = getRoleInStore(storeOwner.getUserId(), appointManager.getUserId(), storeId);
        assertEquals(Role.Manager, role);
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
    private void isGoodRemovePermission(UserInfo storeOwner, UserInfo appointManager, int storeId)
    {
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
        Role role = getRoleInStore(storeOwner.getUserId(), appointManager.getUserId(), storeId);
        assertEquals(Role.Manager, role);
    }

    @Test
    public void AddProductWithoutPermission()
    {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointManager);
        isGoodManagerAppoint(storeOwner, appointManager, storeId);
        // TODO: Remove permission add product:
        // Add product
        ProductInfo pi = createProduct5();
        int status = this.addProduct(appointManager.getUserId(), storeId, pi);
        assertTrue(status < 0);
        //TODO: Check that the product dont exist in the store
    }

    @Test
    public void AppointManagerWithoutPermission()
    {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager1 = this.users_dict.get(users[1][USER_EMAIL]);
        UserInfo appointManager2 = this.users_dict.get(users[2][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointManager1);
        isGoodManagerAppoint(storeOwner, appointManager1, storeId);
        // TODO: Remove permission of appoint manger to appointManager1:
        int status = this.appointmentManagerInStore(appointManager1.getUserId(), storeId, appointManager2.getEmail());
        assertTrue(status > 0);
    }

    @Test
    public void RemoveProductWithoutPermission()
    {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        isGoodLogin(storeOwner);
        isGoodLogin(appointManager);
        // TODO: write strong assert for appoint that check the roles in the store
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
        // TODO: Remove permission add product:
        // Remove product
        // TODO: status = this.removeProduct(appointManager.getUserId(), storeId, pi);
        assertTrue(status < 0);
        //TODO: Check that the product dont exist in the store
    }

}
