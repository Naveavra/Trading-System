package server;

import market.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.security.UserAuth;
import utils.Response;
import utils.infoRelated.LoginInformation;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class APITest {
    private HashMap<Integer, HashMap<Integer, Integer>> cart;
    private LoginInformation li;
    API api;
    int userId;
    String token;
    int storeId;
    String defaultService = "WSEP";
    List<String> categories;
    int productId;
    int productId2;

    @BeforeEach
    void setUp() {
        api = new API();
        Market m = api.market;
        //api.mockData();
//        m.register("eli@gmail.com", "123Aaa", "24/02/2002");
//        userId = m.login("eli@gmail.com", "123Aaa").getValue().getUserId();
//        token = m.addTokenForTests();
//        storeId = m.openStore(userId, token, "eli store", "good store", "img").getValue();
//        categories = new ArrayList<>();
//        categories.add("food");
//        categories.add("hang out");
//        productId = m.addProduct(userId, token, storeId, categories, "burger", "from McDonald", 5, 2, "img").getValue();
//        productId2 = m.addProduct(userId, token, storeId, categories, "burger2", "from McDonald", 5, 2, "img").getValue();
//        m.addProductToCart(userId, storeId, productId, 1);
//        m.addProductToCart(userId, storeId, productId2, 1);
//        m.register("chai@gmail.com", "456Bbb", "01/01/2000");
//        m.appointManager(userId, token, "chai@gmail.com", storeId);
    }

    @Test
    void getCart(){
        System.out.println(api.getCart(userId).getSecond().get("value"));
    }

    @Test
    void getStore(){
        System.out.println(api.getStore(userId, token, storeId).getSecond().get("value"));
    }
//    @Test
//    void getBaskets() {
//        System.out.println(api.getBaskets(cart));
//    }

    @Test
    void logTest() {
        //System.out.println(api.logsToString(logs));
    }

    @Test
    void loginTest() {
//        System.out.println(api.loginToJson(li).toString());
    }

    @Test
    void getSupplierAvailableServices() {
        System.out.println(api.getPaymentAvailableServices().getSecond().get("value"));
        assertTrue(api.getPaymentAvailableServices().getSecond().get("value")
                .toString().contains(defaultService));
    }

    @Test
    void getPaymentAvailableServices() {
        assertTrue(api.getSupplierAvailableServices().getSecond().get("value")
                .toString().contains(defaultService));
    }

    @Test
    void getMarketInfo(){
        api.mockData();
        api.login("elibenshimol6@gmail.com", "123Aaa");
        System.out.println(api.watchMarketStatus(1, api.market.addTokenForTests()).getSecond());
    }

    @Test
    void testHash(){
        UserAuth userAuth = new UserAuth();
        System.out.println(userAuth.hashPassword("eli@gmail.com", "123Aaa"));
    }
}