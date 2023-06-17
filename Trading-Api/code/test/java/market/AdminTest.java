package market;

import database.Dao;
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
        Dao.setForTests(true);
        Admin a = new Admin("eli@gmail.com", "123Aaa");
        market = new Market(a);
        adminId = market.login("eli@gmail.com", "123Aaa").getValue().getUserId();
        token = market.addTokenForTests();
    }

    @Test
    void addAdmin() {
        int size = market.getAdminSize();
        market.addAdmin(adminId, token, "ziv@gmail.com", "456Bbb");
        assertEquals(size+1, market.getAdminSize());
    }

    @Test
    void getAdmins(){
        Response<HashMap<Integer, Admin>> res = market.getAdmins(adminId, token);
        assertFalse(res.errorOccurred());
        assertEquals(1, res.getValue().size());
        assertEquals("eli@gmail.com", res.getValue().get(adminId).getName());

    }

    @Test
    void adminLoginAndGetAdmins(){
        market.addAdmin(adminId, token, "ziv@gmail.com", "456Bbb");
        int adminId2 = market.login("ziv@gmail.com", "456Bbb").getValue().getUserId();
        Response<HashMap<Integer, Admin>> res = market.getAdmins(adminId2, token);
        assertFalse(res.errorOccurred());
        assertEquals(2, res.getValue().size());
    }
    @Test
    void removeAdmin() {
        int size = market.getAdminSize();
        market.addAdmin(adminId, token, "ziv@gmail.com", "456Bbb");
        int adminId2 = market.login("ziv@gmail.com", "456Bbb").getValue().getUserId();
        market.removeAdmin(adminId2, token);
        assertEquals(size, market.getAdminSize());
    }

    @Test
    void closeStorePermanently() {
        market.register("nave@gmail.com", "123Ccc", "01/01/1996");
        int userId = market.login("nave@gmail.com", "123Ccc").getValue().getUserId();
        market.register("eli@gmail.com", "123Aaa", "01/01/1996");
        int userId2= market.login("eli@gmail.com", "123Aaa").getValue().getUserId();
        int sid = market.openStore(userId, token, "nike", "good store", "img").getValue();
        int sid2 = market.openStore(userId2, token, "adidas", "good store2", "img").getValue();
        market.closeStorePermanently(adminId, token, sid);
        Response<Store> res = market.getStore(userId, token, sid);
        assertTrue(res.errorOccurred());
        res = market.getStore(userId, token, sid2);
        assertFalse(res.errorOccurred());
        assertEquals(1, market.displayNotifications(userId, token).getValue().size());
        assertEquals(0, market.getMember(userId, token).getValue().getStoreRoles().size());
    }

    @Test
    void cancelMembership() {
        market.register("nave@gmail.com", "123Ccc", "01/01/1996");
        int login = market.login("nave@gmail.com", "123Ccc").getValue().getUserId();
        market.openStore(login, token, "nike", "good store", "img");
        market.cancelMembership(adminId, token, "nave@gmail.com");
        market.removeUser("nave@gmail.com");
        assertTrue(market.getMember(login, token).errorOccurred());
        Response<Store> res = market.getStore(1, token, 0);
        assertTrue(res.errorOccurred());
    }
}