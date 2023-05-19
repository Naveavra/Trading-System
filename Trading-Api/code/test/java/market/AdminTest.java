package market;

import domain.store.storeManagement.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Response;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    private Market market;
    private String token;
    private int adminId;

    @BeforeEach
    void setUp() {
        market = new Market("eli@gmail.com", "123Aaa");
        adminId = market.adminLogin("eli@gmail.com", "123Aaa").getValue().getUserId();
        token = market.addTokenForTests();
    }

    @Test
    void addAdmin() {
        int size = market.getAdminsize();
        market.addAdmin(adminId, token, "ziv@gmail.com", "456Bbb");
        assertEquals(size+1, market.getAdminsize());
    }

    @Test
    void getAdmins(){
        Response<HashMap<Integer, Admin>> res = market.getAdmins(adminId, token);
        assertFalse(res.errorOccurred());
        assertEquals(1, res.getValue().size());
        assertTrue(res.getValue().get(adminId).getEmailAdmin().equals("eli@gmail.com"));

    }

    @Test
    void adminLoginAndGetAdmins(){
        market.addAdmin(adminId, token, "ziv@gmail.com", "456Bbb");
        int adminId2 = market.adminLogin("ziv@gmail.com", "456Bbb").getValue().getUserId();
        Response<HashMap<Integer, Admin>> res = market.getAdmins(adminId2, token);
        assertFalse(res.errorOccurred());
        assertEquals(2, res.getValue().size());

    }
    @Test
    void removeAdmin() {
        market.addAdmin(adminId, token, "ziv@gmail.com", "456Bbb");
        int adminId2 = market.adminLogin("ziv@gmail.com", "456Bbb").getValue().getUserId();
        market.removeAdmin(-2, token);
        assertEquals(adminId2, market.getAdminsize());
    }

    @Test
    void closeStorePermanently() {
        market.register("nave@gmail.com", "123Ccc", "01/01/1996");
        int userId = market.login("nave@gmail.com", "123Ccc").getValue().getUserId();
        market.openStore(userId, token, "nike", "good store", "img");
        market.closeStorePermanently(adminId, token, 0);
        Response<Store> res = market.getStore(userId, token, 0);
        assertTrue(res.errorOccurred());
        assertEquals(1, market.displayNotifications(userId, token).getValue().size());
        assertEquals(0, market.getMember(userId, token).getValue().getStoreRoles().size());
    }

    @Test
    void cancelMembership() {
        market.register("nave@gmail.com", "123Ccc", "01/01/1996");
        int login = market.login("nave@gmail.com", "123Ccc").getValue().getUserId();
        market.openStore(login, token, "nike", "good store", "img");
        market.cancelMembership(adminId, token, login);
        assertTrue(market.getMember(login, token).errorOccurred());
        Response<Store> res = market.getStore(1, token, 0);
        assertTrue(res.errorOccurred());
    }
}