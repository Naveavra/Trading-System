package market;

import domain.store.storeManagement.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Response;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    private Market market;
    private Admin admin;
    private String token;

    @BeforeEach
    void setUp() {
        market = new Market("eli@gmail.com", "123Aaa");
        market.adminLogin("eli@gmail.com", "123Aaa");
        token = market.addTokenForTests();
    }

    @Test
    void addAdmin() {
        market.addAdmin(-1, token, "ziv@gmail.com", "456Bbb");
        assertEquals(2, market.getAdminsize());
    }

    @Test
    void getAdmins(){
        Response<HashMap<Integer, Admin>> res = market.getAdmins(-1, token);
        assertFalse(res.errorOccurred());
        assertEquals(1, res.getValue().size());
        assertTrue(res.getValue().get(-1).getEmailAdmin().equals("eli@gmail.com"));

    }

    @Test
    void adminLoginAndGetAdmins(){
        market.addAdmin(-1, token, "ziv@gmail.com", "456Bbb");
        market.adminLogin("ziv@gmail.com", "456Bbb");
        Response<HashMap<Integer, Admin>> res = market.getAdmins(-2, token);
        assertFalse(res.errorOccurred());
        assertEquals(2, res.getValue().size());

    }
    @Test
    void removeAdmin() {
        market.addAdmin(-1, token, "ziv@gmail.com", "456Bbb");
        market.adminLogin("ziv@gmail.com", "456Bbb");
        market.removeAdmin(-2, token);
        assertEquals(1, market.getAdminsize());
    }

    @Test
    void closeStorePermanently() {
        market.register("nave@gmail.com", "123Ccc", "01/01/1996");
        market.login("nave@gmail.com", "123Ccc");
        market.openStore(1, token, "nike", "good store", "img");
        market.closeStorePermanently(-1, token, 0);
        Response<Store> res = market.getStore(1, token, 0);
        assertTrue(res.errorOccurred());
        assertEquals(1, market.displayNotifications(1, token).getValue().size());
        assertEquals(0, market.getMember(1, token).getValue().getStoreRoles().size());
    }

    @Test
    void cancelMembership() {
        market.register("nave@gmail.com", "123Ccc", "01/01/1996");
        market.login("nave@gmail.com", "123Ccc");
        market.openStore(1, token, "nike", "good store", "img");
        market.cancelMembership(-1, token, 1);
        assertTrue(market.getMember(1, token).errorOccurred());
        Response<Store> res = market.getStore(1, token, 0);
        assertTrue(res.errorOccurred());
    }
}