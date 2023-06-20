package junit;

import data.LoginData;
import data.PermissionInfo;
import data.ProductInfo;
import data.UserInfo;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     actions.add(Action.viewMessages);
     actions.add(Action.answerMessage);
     actions.add(Action.seeStoreHistory);
     actions.add(Action.seeStoreOrders);
     actions.add(Action.checkWorkersStatus);
     permission.addActions(actions);


     addedActions.add(Action.viewMessages);
     addedActions.add(Action.answerMessage);
     addedActions.add(Action.seeStoreHistory);
     addedActions.add(Action.seeStoreOrders);
     addedActions.add(Action.checkWorkersStatus);
     addedActions.add(Action.changeStoreDetails);
     addedActions.add(Action.changePurchasePolicy);
     addedActions.add(Action.changeDiscountPolicy);
     addedActions.add(Action.addPurchaseConstraint);
     addedActions.add(Action.addDiscountConstraint);
     addedActions.add(Action.addProduct);
     addedActions.add(Action.removeProduct);
     addedActions.add(Action.updateProduct);
     */


    /**
     * Store Manager Permissions:
     * 1. Appoint Manager
     * 2. Add Product
     * 3.
     *
     **/

    @Test
    private void assertGoodLogin(UserInfo ui)
    {
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        assertTrue(ui.getUserId() > 0);
    }

    @Test
    private void assertGoodProductRemoving(int userId, int storeId, ProductInfo pi) {
        List pre = getProductInStore(storeId);
        assertNotNull(pre);
        assertProductInList(pi, pre);
        assertTrue(removeProduct(userId, storeId, pi.getProductId()));
        List<ProductInfo> post = getProductInStore(storeId);
        assertNotNull(post);
        assertProductNotInList(pi.getProductId(), pre);
    }

    @Test
    private void assertBadProductRemoving(int userId, int storeId, ProductInfo pi) {
        List pre = getProductInStore(storeId);
        assertNotNull(pre);
        assertFalse(removeProduct(userId, storeId, pi.getProductId()));
        List<ProductInfo> post = getProductInStore(storeId);
        assertNotNull(post);
        assertProductInList(pi, post);
    }

    @Test
    private void assertGoodManagerAppoint(UserInfo storeOwner, UserInfo appointManager, int storeId)
    {
        int status = this.appointmentManagerInStore(storeOwner.getUserId(), storeId, appointManager.getEmail());
        assertTrue(status > 0);
        Role role = getRoleInStore(storeOwner.getUserId(), appointManager.getUserId(), storeId);
        assertNotNull(role);
        assertEquals(Role.Manager, role);
    }

    @Test
    private void isBadManagerAppoint(UserInfo appoint, UserInfo appointedManager, int storeId)
    {
        int status = this.appointmentManagerInStore(appoint.getUserId(), storeId, appointedManager.getEmail());
        assertTrue(status > 0);
        Role role = getRoleInStore(appoint.getUserId(), appointedManager.getUserId(), storeId);
        assertNull(role);
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
    private void assertGoodRemovePermission(UserInfo storeOwner, UserInfo appointManager, int storeId, Action action)
    {
        List<Integer> per2Remove = new ArrayList<>();
        per2Remove.add(action.ordinal());
        assertTrue(removeStoreManagerPermissions(storeOwner.getUserId(), storeId, appointManager.getUserId(), per2Remove));
        PermissionInfo permissions = getManagerPermissionInStore(storeOwner.getUserId(), appointManager.getUserId(), storeId);
        assertNotNull(permissions);
        assertFalse(permissions.havePermission(action));
    }

    @Test
    private void assertGoodAddPermission(UserInfo storeOwner, UserInfo appointManager, int storeId, Action action)
    {
        List<Integer> per2Add = new ArrayList<>();
        per2Add.add(action.ordinal());
        assertTrue(addStoreManagerPermissions(storeOwner.getUserId(), storeId, appointManager.getUserId(), per2Add));
        PermissionInfo permissions = getManagerPermissionInStore(storeOwner.getUserId(), appointManager.getUserId(), storeId);
        assertNotNull(permissions);
        assertTrue(permissions.havePermission(action));
    }

    @Test
    private void assertGoodProductAdding(int userId, int storeId, ProductInfo pi)
    {
        List pre = getProductInStore(storeId);
        assertNotNull(pre);
        pi.setProductId(addProduct(userId, storeId, pi));
        assertTrue(pi.getProductId() > 0);
        List<ProductInfo> post = getProductInStore(storeId);
        assertNotNull(post);
        assertProductInList(pi, post);
    }

    @Test
    private void assertBadProductAdding(int userId, int storeId, ProductInfo pi)
    {
        List pre = getProductInStore(storeId);
        assertNotNull(pre);
        assertTrue(addProduct(userId, storeId, pi) < 0);
        List<ProductInfo> post = getProductInStore(storeId);
        assertNotNull(post);
        assertProductNotInList(pi.getProductId(), post);
        assertEquals(pre.size(), post.size());
    }

    @Test
    private void assertProductInList(ProductInfo pi, List<ProductInfo> products)
    {
        boolean ans = false;
        for(ProductInfo product: products){
            ans = ans || product.equals(pi);
        }
        assertTrue(ans);
    }

    @Test
    private void assertProductNotInList(int productId, List<ProductInfo> products)
    {
        boolean ans = false;
        for(ProductInfo product: products){
            ans = ans || product.getProductId() == productId;
        }
        assertFalse(ans);
    }

    @Test
    public void AddProductWithPermission()
    {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        assertGoodLogin(storeOwner);
        assertGoodLogin(appointManager);
        // Appoint Manager
        assertGoodManagerAppoint(storeOwner, appointManager, storeId);
        // Add permission:
        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.addProduct);
        // Add product:
        ProductInfo pi = createProduct5();
        assertGoodProductAdding(storeOwner.getUserId(), storeId, pi);
    }

    @Test
    public void AddProductWithoutPermission()
    {
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        assertGoodLogin(storeOwner);
        assertGoodLogin(appointManager);
        // Appoint Manager
        assertGoodManagerAppoint(storeOwner, appointManager, storeId);
        // Add permission:
        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.addProduct);
        // Remove permission add product:
        assertGoodRemovePermission(storeOwner, appointManager, storeId, Action.addProduct);
        // Add product
        ProductInfo pi = createProduct5();
        assertBadProductAdding(appointManager.getUserId(), storeId, pi);
    }

    @Test
    public void RemoveProductWithPermission()
    {
        ProductInfo pi = createProduct5();
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        assertGoodLogin(storeOwner);
        assertGoodLogin(appointManager);
        // Appoint Manager
        assertGoodManagerAppoint(storeOwner, appointManager, storeId);
        // Add permissions:
        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.addProduct);
        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.removeProduct);
        // Add product:
        assertGoodProductAdding(storeOwner.getUserId(), storeId, pi);
        // Remove product
        assertGoodProductRemoving(appointManager.getUserId(), storeId, pi);
    }

    @Test
    public void RemoveProductWithoutPermission()
    {
        ProductInfo pi = createProduct5();
        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
        int storeId = stores.get(0).getStoreId();
        assertGoodLogin(storeOwner);
        assertGoodLogin(appointManager);
        // Appoint Manager
        assertGoodManagerAppoint(storeOwner, appointManager, storeId);
        // Add permissions:
        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.addProduct);
        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.removeProduct);
        // Add product:
        assertGoodProductAdding(storeOwner.getUserId(), storeId, pi);
        // Remove permission add product:
        assertGoodRemovePermission(storeOwner, appointManager, storeId, Action.removeProduct);
        // Remove product
        assertBadProductRemoving(appointManager.getUserId(), storeId, pi);
    }

//    TODO:
//    @Test
//    public void UpdateProductWithPermission()
//    {
//        ProductInfo pi = createProduct5();
//        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
//        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
//        int storeId = stores.get(0).getStoreId();
//        assertGoodLogin(storeOwner);
//        assertGoodLogin(appointManager);
//        // Appoint Manager
//        assertGoodManagerAppoint(storeOwner, appointManager, storeId);
//        // Add permissions:
//        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.addProduct);
//        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.updateProduct);
//        // Add product:
//        assertGoodProductAdding(storeOwner.getUserId(), storeId, pi);
//        // Remove product
//        assertGoodProductRemoving(appointManager.getUserId(), storeId, pi);
//    }
//
//    @Test
//    public void UpdateProductWithoutPermission()
//    {
//        ProductInfo pi = createProduct5();
//        UserInfo storeOwner = this.users_dict.get(users[0][USER_EMAIL]);
//        UserInfo appointManager = this.users_dict.get(users[1][USER_EMAIL]);
//        int storeId = stores.get(0).getStoreId();
//        assertGoodLogin(storeOwner);
//        assertGoodLogin(appointManager);
//        // Appoint Manager
//        assertGoodManagerAppoint(storeOwner, appointManager, storeId);
//        // Add permissions:
//        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.addProduct);
//        assertGoodAddPermission(storeOwner, appointManager, storeId, Action.removeProduct);
//        // Add product:
//        assertGoodProductAdding(storeOwner.getUserId(), storeId, pi);
//        // Remove permission add product:
//        assertGoodRemovePermission(storeOwner, appointManager, storeId, Action.removeProduct);
//        // Remove product
//        assertBadProductRemoving(appointManager.getUserId(), storeId, pi);
//    }

}
