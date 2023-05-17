package market;

import domain.states.StoreCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.API;
import utils.Logger;
import utils.LoginInformation;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    private Market market;
    private Admin admin;
    private String token;

    @BeforeEach
    void setUp() {
        admin = new Admin(-1, "eli@gmail.com", "123Aaa");
        market = new Market(admin);
        token = market.addTokenForTests();
    }

    @Test
    void addAdmin() {
        market.addAdmin(-1, token, "ziv@gmail", "456Bbb");
        assertEquals(2, market.getAdminsize());
    }

    @Test
    void adminLogin(){

    }
    @Test
    void removeAdmin() {
        market.addAdmin(-1, token, "ziv@gmail", "456Bbb");

        market.removeAdmin(-2, token);
        assertEquals(2, market.getAdminsize());
    }

    @Test
    void closeStorePermanently() {
    }

    @Test
    void checkEmail() {
    }

    @Test
    void checkPassword() {
    }

    @Test
    void addControllers() {
    }

    @Test
    void cancelMembership() {
    }
}