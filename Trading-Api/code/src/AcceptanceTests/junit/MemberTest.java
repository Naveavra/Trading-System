package junit;

import data.CartInfo;
import data.UserInfo;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemberTest extends ProjectTest{
    private static final int ERROR = -1;
    private JSONObject payment = createPaymentJson();
    private JSONObject supplier = createSupplierJson();

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
     * Member:
     * logout - V
     * NTH:
     * Write Review -
     * Send question to store -
     * Get Info Purchase history -
     * get member info
     * change privacy details
     * Secure connect  to system
     * + Guest test - V
     * **/

    //Logout:

    @Test
    public void testLogout(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        int status = logout(buyer.getUserId());
        assertTrue(status > 0);
    }

    @Test
    public void testLogoutBeforeLogin(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        int status = logout(buyer.getUserId());
        assertTrue(status < 0);
    }

    @Test
    public void testUnExistLogout(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        int status = logout(200);
        assertTrue(status < 0);
    }

    //Purchase Cart:
    @Test
    public void testPurchaseCart(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //make purchase
        status  = makePurchase(buyer.getUserId(), payment, supplier);
        assertTrue(status > 0);
    }

    @Test
    public void testPurchaseFromUnAvailablePSCart(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        status = this.addExternalPaymentService(mainAdmin.getAdminId(), "Apple Pay");
        assertTrue(status > 0);
        status = this.replaceExternalPaymentService(mainAdmin.getAdminId(), "Apple Pay");
        assertTrue(status > 0);
        //make purchase
        status  = makePurchase(buyer.getUserId(), payment, supplier);
        assertTrue(status < 0);
    }

    @Test
    public void testPurchaseFromUnAvailableSSCart(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        status = this.addExternalSupplierService(mainAdmin.getAdminId(), "UPS");
        assertTrue(status > 0);
        status = this.replaceExternalSupplierService(mainAdmin.getAdminId(), "UPS");
        assertTrue(status > 0);
        //make purchase
        status  = makePurchase(buyer.getUserId(), payment, supplier);
        assertTrue(status < 0);
    }

    @Test
    public void testPurchaseCartNoAtSameTime(){
        UserInfo buyer1 = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo buyer2 = this.users_dict.get(users[1][USER_EMAIL]);
        //UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer1.setUserId(login(buyer1.getEmail(), buyer1.getPassword()));
        buyer2.setUserId(login(buyer2.getEmail(), buyer2.getPassword()));
        //uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        int status = addProductToCart(buyer1.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), pi5s4.getQuantity());
        assertTrue(status > -1);
        status = addProductToCart(buyer2.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 2);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer1.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //Check the cart:
        ci = getCart(buyer2.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //make purchase
        int status1 = makePurchase(buyer1.getUserId(), payment, supplier);
        int status2 = makePurchase(buyer2.getUserId(), payment, supplier);
        assertNotEquals(status1, status2);
    }

    @Test
    public void testPurchaseCartSameTime(){
        UserInfo buyer1 = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo buyer2 = this.users_dict.get(users[1][USER_EMAIL]);
     //   UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer1.setUserId(login(buyer1.getEmail(), buyer1.getPassword()));
        buyer2.setUserId(login(buyer2.getEmail(), buyer2.getPassword()));
     //   uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        int status = addProductToCart(buyer1.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        status = addProductToCart(buyer2.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        //Check the cart:
        CartInfo ci = getCart(buyer1.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //Check the cart:
        ci = getCart(buyer2.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
        //make purchase
        AtomicInteger i = new AtomicInteger();
        Thread t1 = new Thread(() -> {
            i.addAndGet(makePurchase(buyer1.getUserId(), payment, supplier));
        });
        Thread t2 = new Thread(() -> {
            i.addAndGet(makePurchase(buyer2.getUserId(), payment, supplier));
        });
        t1.start();
        t2.start();
        assertTrue(i.get() == 0);
    }

    //Get Cart:

    @Test
    public void testGetCart(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        CartInfo ci = getCart(buyer.getUserId());
        assertNotNull(ci);
    }

    @Test
    public void testGetCartAfterAddProduct(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Check the cart:
        CartInfo ci = getCart(buyer.getUserId());
        assertNotNull(ci);
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
        ci = getCart(buyer.getUserId());
        assertNotNull(ci);
        assertTrue(ci.getCountOfProduct() > 0);
    }

    @Test
    public void testGetCartOfUnlogingUser(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Check the cart:
        CartInfo ci = getCart(buyer.getUserId());
        assertNull(ci);
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
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 1);
        assertTrue(status > -1);
    }

    @Test
    public void testBadTryAddProductToCart(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), ERROR);
        assertEquals(-1, status);
    }


    /*
    @Test
    public void testAddMoreThanExistsProductToCart(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), pi5s4.getProductId(), 2);
        assertTrue(status < -1);
    }
     */


    @Test
    public void testAddUnExistsProductToCart(){
        UserInfo buyer = this.users_dict.get(users[0][USER_EMAIL]);
        UserInfo uid = this.users_dict.get(users[1][USER_EMAIL]);//Owner of store 4
        //Login
        buyer.setUserId(login(buyer.getEmail(), buyer.getPassword()));
        uid.setUserId(login(uid.getEmail(), uid.getPassword()));
        //Add product to cart
        int status = addProductToCart(buyer.getUserId(), stores.get(4).getStoreId(), ERROR, 2);
        assertTrue(status == -1);
    }


    //Login:

    @Test
    public void testLoginSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2000");
        assertTrue(id > -1);
        int status = login("hello123@gmail.com", "hello123A");
        assertTrue(status > -1);
    }

    @Test
    public void testAlreadyLoggedInSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2000");
        assertTrue(id > -1);
        int status = login("hello123@gmail.com", "hello123A");
        assertTrue(status > -1);
        status = login("hello123@gmail.com", "hello123A");
        assertTrue(status < 0);
    }

    @Test
    public void testWrongLoginSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2000");
        assertTrue(id > -1);
        int status = login("hello123@gmail.com", "hello12123A");
        assertTrue(status < 0);
    }

    @Test
    public void testUnExistUserLoginSystem(){
        int status = login("hello123@gmail.com", "hello12123A");
        assertTrue(status < 0);
    }

    //Register

    @Test
    public void testRegisterSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2000");
        assertTrue(id > -1);
    }

    @Test
    public void testRegisterExistMailSystem(){
        int id = register("hello123@gmail.com", "hello123A", "01/01/2000");
        assertTrue(id > -1);
        id = register("hello123@gmail.com", "hello123A", "01/01/2000");
        assertTrue(id < 0);
    }
}
