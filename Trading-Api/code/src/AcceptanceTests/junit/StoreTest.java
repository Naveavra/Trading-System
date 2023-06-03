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
    public StoreInfo goodCreatingStore(StoreInfo si)
    {
        StoreInfo store = null;
        si.setStoreId(createStore(si));
        assertTrue(si.getStoreId() > 0);
        store = getStore(si.getStoreId());
        assertNotNull(store);
        store.equals(si);
        return store;
    }

    @Test
    private void badCreatingStore(StoreInfo si)
    {
        StoreInfo store = null;
        si.setStoreId(createStore(si));
        assertTrue(si.getStoreId() < 0);
        store = getStore(si.getStoreId());
        assertNull(store);
    }

    @Test
    private void isGoodLogin(UserInfo ui)
    {
        UserInfo user = null;
        ui.setUserId(login(ui.getEmail(), ui.getPassword()));
        assertTrue(ui.getUserId() > 0);
    }


    @Test
    public void testCreateStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        isGoodLogin(ui);
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        goodCreatingStore(si);
    }

    @Test
    public void testWithoutLoginCreateStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        badCreatingStore(si);
    }

    //Get products in store:

    @Test
    private List<ProductInfo> goodGettingProductList(int storeId)
    {
        List<ProductInfo> products = getProductInStore(storeId);
        assertNotNull(products);
        return products;
    }

    @Test
    public void testGetProductInStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        isGoodLogin(ui);
        StoreInfo si = new StoreInfo(ui.getUserId(),"s", "Gong", "img");
        si = goodCreatingStore(si);
        List<ProductInfo> products = goodGettingProductList(si.getStoreId());
        assertTrue(products.isEmpty());
    }

    @Test
    private void goodCloseStore(int userId, int storeId)
    {
        assertTrue(closeStore(userId, storeId));
    }

    @Test
    public void testGetProductFromCloseStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        isGoodLogin(ui);
        StoreInfo si = new StoreInfo(ui.getUserId(),"s", "Gong", "img");
        si = goodCreatingStore(si);
        goodCloseStore(ui.getUserId(), si.getStoreId());
        List<ProductInfo> products = goodGettingProductList(si.getStoreId());
        assertTrue(products.isEmpty());
    }

    @Test
    private void goodProductAdding(int userId, int storeId, ProductInfo pi)
    {
        List pre = getProductInStore(storeId);
        assertNotNull(pre);
        pi.setProductId(addProduct(userId, storeId, pi));
        assertTrue(pi.getProductId() > 0);
        List<ProductInfo> post = getProductInStore(storeId);
        assertNotNull(post);
        productInList(pi, post);
    }

    private ProductInfo getProduct(int productId,  List<ProductInfo> products)
    {
        for(ProductInfo product: products)
        {
            if (product.getProductId() == productId)
            {
                return product;
            }
        }
        return null;
    }

    @Test
    private void goodProductRemoving(int userId, int storeId, int productId)
    {
        List pre = getProductInStore(storeId);
        assertNotNull(pre);
        ProductInfo pi = getProduct(productId, pre);
        assertNotNull(pi);
        assertTrue(removeProduct(userId, storeId, productId));
        List<ProductInfo> post = getProductInStore(storeId);
        assertNotNull(post);
        assertEquals(pre.size() - 1, post.size());
        productNotInList(productId, post);
    }

    @Test
    private void productInList(ProductInfo pi, List<ProductInfo> products)
    {
        boolean ans = false;
        for(ProductInfo product: products){
            ans = ans || product.equals(pi);
        }
        assertTrue(ans);
    }

    @Test
    private void productNotInList(int productId, List<ProductInfo> products)
    {
        boolean ans = false;
        for(ProductInfo product: products){
            ans = ans || product.getProductId() == productId;
        }
        assertFalse(ans);
    }

    @Test
    public void testGetProductInStoreAfterAddProduct() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        isGoodLogin(ui);
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        si = goodCreatingStore(si);
        List<ProductInfo> products = getProductInStore(si.getStoreId());
        assertNotNull(products);
        ProductInfo pi = createProduct0();
        goodProductAdding(ui.getUserId(), si.getStoreId(), pi);
    }

    @Test
    public void testGetProductInStoreAfterRemoveProduct() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        // Login:
        isGoodLogin(ui);
        // Create Store:
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        si = goodCreatingStore(si);
        List<ProductInfo> products = getProductInStore(si.getStoreId());
        assertNotNull(products);
        ProductInfo pi = createProduct0();
        // Add product to store
        goodProductAdding(ui.getUserId(), si.getStoreId(), pi);
        // Remove product from the store
        goodProductRemoving(ui.getUserId(), si.getStoreId(), pi.getProductId());
    }

    @Test
    public void testGetProductInNewStore() {
        UserInfo ui = users_dict.get(users[0][USER_EMAIL]);
        // Login:
        isGoodLogin(ui);
        // Create Store:
        StoreInfo si = new StoreInfo(ui.getUserId(), "s", "Gong", "img");
        si = goodCreatingStore(si);
        ProductInfo pi = createProduct0();
        // Add product to store
        goodProductAdding(ui.getUserId(), si.getStoreId(), pi);
        // Get the added product
        assertTrue(pi.equals(getProduct(pi.getProductId(),
                getProductInStore(si.getStoreId()))));
    }

    //Get Store:

    @Test
    public void testGetStore() {
        StoreInfo storeTest = stores.get(0);
        StoreInfo store = this.getStoreInfo(storeTest.getStoreId());
        assertNotNull(store);
        storeTest.equals(store);
    }

    private UserInfo getUserInfo(int userId){
        for (UserInfo user : users_dict.values())
        {
            if(user.getUserId() == userId)
            {
                return user;
            }
        }
        return null;
    }

    @Test
    public void testCloseStore() {
        StoreInfo storeTest = stores.get(0);
        UserInfo ui = getUserInfo(storeTest.getCreatorId());
        isGoodLogin(ui);
        assertTrue(closeStore(ui.getUserId(), storeTest.getStoreId()));
        StoreInfo askStore = this.getStore(storeTest.getStoreId());
        assertFalse(askStore.isActive());
    }

    @Test
    public void testBadCloseStore() {
        StoreInfo storeTest = stores.get(0);
        UserInfo ui = getUserInfo(storeTest.getCreatorId());
        assertFalse(closeStore(ui.getUserId(), storeTest.getStoreId()));
        StoreInfo askStore = this.getStore(storeTest.getStoreId());
        assertTrue(askStore.isActive());
    }

    @Test
    public void testGetCloseStore() {
        StoreInfo storeTest = stores.get(0);
        UserInfo ui = getUserInfo(storeTest.getCreatorId());
        isGoodLogin(ui);
        assertTrue(closeStore(ui.getUserId(), storeTest.getStoreId()));
        StoreInfo askStore = getStoreInfo(storeTest.getStoreId());
        assertNull(askStore);
    }

    @Test
    public void testGetUnExistStore() {
        StoreInfo status = this.getStore(ERROR);
        assertNull(status);
    }
}
