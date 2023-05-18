package junit;

import data.AdminInfo;
import data.ProductInfo;
import data.StoreInfo;
import data.UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest extends ProjectTest{

    private static final int ERROR = -1;
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

    /**     Store Functionality
     * 1. Get store - V
     * 2. Get products in store -
     * Nice To Test:
     *
     */

    //Create Store:

    @Test
    public void testCreateStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        assertTrue(ui.getUserId() > 0);
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        si.setStoreId(createStore(si));
        assertTrue(si.getStoreId() > 0);
    }

    @Test
    public void testWithoutLoginCreateStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        si.setStoreId(createStore(si));
        assertTrue(si.getStoreId() < 0);
    }

    //Get products in store:

    @Test
    public void testGetProductInStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        StoreInfo si = new StoreInfo(ui.getUserId(),"s", "Gong", "img");
        si.setStoreId(createStore(si));
        List<ProductInfo> products = getProductInStore(si.getStoreId());
        assertNotNull(products);
    }

    @Test
    public void testGetProductInStoreAfterAddProduct() {
        //TODO:
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        si.setStoreId(createStore(si));
        List<ProductInfo> products = getProductInStore(si.getStoreId());
        assertNotNull(products);
    }

    @Test
    public void testGetProductInStoreAfterRemoveProduct() {
        //TODO:
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        StoreInfo si = new StoreInfo(ui.getUserId(),"s", "Gong", "img");
        si.setStoreId(createStore(si));
        List<ProductInfo> products = getProductInStore(si.getStoreId());
        assertNotNull(products);
    }

    //Get Store:

    @Test
    public void testGetStore() {
        int storeId = stores.get(0).getStoreId();
        StoreInfo status = this.getStore(storeId);
        assertNotNull(status);
    }

    @Test
    public void testGetUnexistStore() {
        StoreInfo status = this.getStore(ERROR);
        assertNull(status);
    }
}
