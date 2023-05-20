package server;

import domain.states.StoreCreator;
import jdk.jfr.Category;
import market.Market;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Logger;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.Receipt;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.*;

class APITest {
    private HashMap<Integer, HashMap<Integer, Integer>> cart;
    private HashMap<Logger.logStatus, List<String>> logs;
    private LoginInformation li;
    API api;
    int userId;
    String token;
    int storeId;

    List<String> categories;
    int productId;
    int productId2;

    @BeforeEach
    void setUp() {
        api = new API();
        Market m = api.market;
        api.mockData();
        m.register("eli@gmail.com", "123Aaa", "24/02/2002");
        userId = m.login("eli@gmail.com", "123Aaa").getValue().getUserId();
        token = m.addTokenForTests();
        storeId = m.openStore(userId, token, "eli store", "good store", "img").getValue();
        categories = new ArrayList<>();
        categories.add("food");
        categories.add("hang out");
        productId = m.addProduct(userId, token, storeId, categories, "burger", "from McDonald", 5, 2, "img").getValue();
        productId2 = m.addProduct(userId, token, storeId, categories, "burger2", "from McDonald", 5, 2, "img").getValue();
        m.addProductToCart(userId, storeId, productId, 1);
        m.addProductToCart(userId, storeId, productId2, 1);
    }

    @Test
    void getCart(){
        System.out.println(api.getCart(userId).getSecond().get("value"));
    }
//    @Test
//    void getBaskets() {
//        System.out.println(api.getBaskets(cart));
//    }

    @Test
    void logTest() {
//        System.out.println(api.logsToString(logs));
    }

    @Test
    void loginTest() {
//        System.out.println(api.loginToJson(li).toString());
    }

}