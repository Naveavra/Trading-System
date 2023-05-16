package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Logger;
import utils.LoginInformation;
import utils.stateRelated.Role;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class APITest {
    private HashMap<Integer, HashMap<Integer, Integer>> cart;
    private HashMap<Logger.logStatus, List<String>> logs;
    private LoginInformation li;
    private API api;
    @BeforeEach
    void setUp() {
        api = new API();
        cart = new HashMap<>();
        logs = new HashMap<>();
        List<String> noti = new ArrayList<>();
        HashMap<Integer, Role> sr = new HashMap<>();
        HashMap<Integer, String> sn = new HashMap<>();
        HashMap<Integer, String> si = new HashMap<>();
        noti.add("Hello");
        noti.add("World");
        sr.put(0, Role.Creator);
        sn.put(0, "Role.Creator");
        si.put(0, "Role.Creator");
        li = new LoginInformation("5513", 1, "55", true, noti, false, sr, sn, si);
        logs.put(Logger.logStatus.Success, new ArrayList<>());
        cart.put(1, new HashMap<>());
        logs.get(Logger.logStatus.Success).add("CREATE cart");
        cart.get(1).put(1, 1);
        logs.get(Logger.logStatus.Success).add("ADD cart (1, 1)");
        cart.put(5, new HashMap<>());
        cart.get(5).put(5, 5);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBaskets() {
        System.out.println(api.getBaskets(cart));
    }

    @Test
    void logTest() {
//        System.out.println(api.logsToString(logs));
    }

    @Test
    void loginTest() {
//        System.out.println(api.loginToJson(li).toString());
    }
}