package junit;

import data.*;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class GuestTest extends ProjectTest{
    private ProductInfo goodProduct0;
    private ProductInfo goodProduct1;
    private JSONObject payment = createPaymentJson();
    private JSONObject supplier = createSupplierJson();


    private static final int ERROR = -1;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        this.goodProduct0 = createProduct0();
        this.goodProduct1 = createProduct1();
    }

    @Override
    @AfterEach
    public void tearDown() {
        super.tearDown();
    }

//    public Response removeProductFromCart(int userId,  int storeId, int productId);
//    public Response changeQuantityInCart(int userId, int storeId, int productId, int change);
//    public Response getStoreDescription(int storeId);

    /**
     * Guest:
     * --------------------
     * enterToSystem - V
     * leaveSystem - V
     * register - V
     * login - V
     * Search product - TODO: Wait for Miki Implementation
     * Add product to cart - V
     * Check Cart - V
     * purchase the cart - V
     * Edit Cart -
     *
     **/


    //Edit Cart:

    @Test
    public void testRemoveProductFromCart(){
        GuestInfo buyer = new GuestInfo();
        //Login
        buyer.setId(enterSystem());
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        status = removeProductFromCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId());
        assertTrue(status > 0);
        ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() == 0);
    }

    @Test
    public void testRemoveUnexistProductFromCart(){
        GuestInfo buyer = new GuestInfo();
        //Login
        buyer.setId(enterSystem());
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        status = removeProductFromCart(buyer.getId(), stores.get(4).getStoreId(), ERROR);
        assertTrue(status < 0);
        ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
    }

    @Test
    public void testUpdateProductFromCart(){
        GuestInfo buyer = new GuestInfo();
        //Login
        buyer.setId(enterSystem());
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        status = changeQuantityInCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > 0);
        ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
    }

    @Test
    public void testUpdateUnexistProductInCart(){
        GuestInfo buyer = new GuestInfo();
        //Login
        buyer.setId(enterSystem());
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        status = changeQuantityInCart(buyer.getId(), stores.get(4).getStoreId(), ERROR, 2);
        assertTrue(status < 0);
        ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
    }

    //Get data:

    @Test
    public void testGetStoreDescription(){
        GuestInfo buyer = new GuestInfo();
        //Login
        buyer.setId(enterSystem());
        assertEquals(stores.get(0).getDescription(), getStore(stores.get(0).getStoreId()).getDescription());
    }

    //Purchase the cart:

    @Test
    public void testPurchaseCart(){
        GuestInfo buyer = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //make purchase
        status  = makePurchase(buyer.getId(), payment, supplier);
        assertNotEquals(ERROR, status);
    }


    @Test
    public void testPurchaseCartNoAtSameTime(){
        GuestInfo buyer1 = new GuestInfo();
        GuestInfo buyer2 = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer1.setId(enterSystem());
        buyer2.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        int status = addProductToCart(buyer1.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        status = addProductToCart(buyer2.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer1.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //Check the cart:
        ci = getCart(buyer2.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //make purchase
        int status1 = makePurchase(buyer1.getId(), payment, supplier);
        int status2 = makePurchase(buyer2.getId(), payment, supplier);
        assertNotEquals(ERROR, status1);
        assertEquals(ERROR, status2);
    }

    @Test
    public void testPurchaseCartSameTime(){
        GuestInfo buyer1 = new GuestInfo();
        GuestInfo buyer2 = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer1.setId(enterSystem());
        buyer2.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        int status = addProductToCart(buyer1.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        status = addProductToCart(buyer2.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer1.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //Check the cart:
        ci = getCart(buyer2.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //make purchase
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(makePurchase(buyer1.getId(), payment, supplier));
        });
        Thread t2 = new Thread(() -> {
            i.addAndGet(makePurchase(buyer2.getId(), payment, supplier));
        });
        t1.start();
        t2.start();
        assertTrue(i.get() <= 0);
    }

    //Get Cart:

    @Test
    public void testGetCart(){
        GuestInfo buyer = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
    }

    @Test
    public void testGetCartAfterAddProduct(){
        GuestInfo buyer = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        ci = getCart(buyer.getId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
    }

    @Test
    public void testGetCartOfUnlogingUser(){
        GuestInfo buyer = new GuestInfo();
        buyer.setId(enterSystem());
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Check the cart:
        CartInfo ci = getCart(buyer.getId());
        assertNotNull(ci);
        CartInfo ci2 = getCart(uid.getUserId());
        assertNull(ci2);
    }

    @Test
    public void testGetCartOfAdmin(){
        CartInfo ci = getCart(mainAdmin.getAdminId());
        assertNull(ci);
    }

    @Test
    public void testGetCartOfUnExistUser(){
        CartInfo ci = getCart(200);
        assertNull(ci);
    }

    //Add product to cart
    @Test
    public void testAddProductToCart(){
        GuestInfo buyer = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
    }

    @Test
    public void testBadTryAddProductToCart(){
        GuestInfo buyer = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), ERROR);
        assertTrue(status == -1);
    }

    /*
    @Test
    public void testAddMoreThanExistsProductToCart(){
        GuestInfo buyer = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 2);
        assertTrue(status < -1);
    }
     */

    @Test
    public void testAddUnExistsProductToCart(){
        GuestInfo buyer = new GuestInfo();
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setId(enterSystem());
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getId(), stores.get(4).getStoreId(), ERROR, 2);
        assertTrue(status <= -1);
    }


    //Login:

    @Test
    public void testLoginSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2002");
        assertTrue(id > -1);
        int status = login("hello123@gmail.com", "hello123A");
        assertTrue(status > -1);
    }

    @Test
    public void testAlreadyLoggedInSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2002");
        assertTrue(id > -1);
        int status = login("hello123@gmail.com", "hello123A");
        assertTrue(status > -1);
        status = login("hello123@gmail.com", "hello123A");
        assertTrue(status < 0);
    }

    @Test
    public void testWrongLoginSystem(){
        int id = register("hello123@gmail.com", "hellAo123", "01/01/2002");
        assertTrue(id > -1);
        int status = login("hello123@gmail.com", "hello123A");
        assertTrue(status < 0);
    }

    @Test
    public void testUnExistUserLoginSystem(){
        int status = login("hello123@gmail.com", "hello123A");
        assertTrue(status < 0);
    }

    //Register

    @Test
    public void testRegisterSystem(){
        int id = register("hello123@gmail.com", "hello12A3", "01/01/2002");
        assertTrue(id > -1);
    }
    @Test
    public void testRegisterIllegalPasswordSystem(){
        int id = register("hello123@gmail.com", "hello123", "01/01/2002");
        assertTrue(id < 0);
    }

    @Test
    public void testRegisterIllegalPasswordSystemBar(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2002");
        int id2 = register("hello123@gmail.com", "hello123AA", "01/01/2002");
        assertTrue(id > 0);
        assertTrue(id2 < 0);
    }
    @Test
    public void testRegisterIllegalBrithdaySystem(){
        int id = register("hello123@gmail.com", "hello123", "01/01/100");
        assertTrue(id < 0);
    }
    @Test
    public void testRegisterIllegalMailSystem(){
        int id = register("hello123", "hello123", "01/01/2002");
        assertTrue(id < 0);
    }
    @Test
    public void testRegisterExistMailSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2002");
        assertTrue(id > -1);
        id = register("hello123@gmail.com", "hello123A", "01/01/2002");
        assertTrue(id < 0);
    }

    //Leave System

    @Test
    public void testLeaveSystem(){
        int id = enterSystem();
        assertTrue(id > -1);
        CartInfo ci =  getCart(id);
        assertNotNull(ci);
        exitSystem(id);
        ci =  getCart(id);
        assertNull(ci);
    }

    //Enter System

    @Test
    public void testEnterSystem(){
        int id = enterSystem();
        assertTrue(id > -1);
    }
}
