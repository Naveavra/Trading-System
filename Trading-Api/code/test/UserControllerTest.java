import database.HibernateUtil;
import database.daos.Dao;
import domain.states.Permissions;
import domain.states.UserState;
import domain.store.storeManagement.AppHistory;
import domain.store.storeManagement.Store;
import domain.user.Member;
import domain.user.StringChecks;
import market.Admin;
import market.Market;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import server.Config.ConfigParser;
import service.MarketController;
import service.UserController;
import utils.Pair;
import utils.infoRelated.LoginInformation;
import utils.infoRelated.Receipt;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;
import utils.stateRelated.Action;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private JSONObject payment = createPaymentJson();
    private JSONObject supplier = createSupplierJson();
    private JSONObject createPaymentJson()
    {
        JSONObject payment = new JSONObject();
        payment.put("payment_service", "WSEP");
        payment.put("cardNumber", "123456789");
        payment.put("month", "01");
        payment.put("year", "30");
        payment.put("holder", "Israel Visceral");
        payment.put("ccv", "000");
        payment.put("id", "123456789");
        return payment;
    }

    private  JSONObject createSupplierJson()
    {
        JSONObject supplier = new JSONObject();
        supplier.put("supply_service", "WSEP");
        supplier.put("name", "Israel Visceral");
        supplier.put("address", "Reger 17");
        supplier.put("city", "Beer Sheva");
        supplier.put("country", "Israel");
        supplier.put("zip", "700000");
        return supplier;
    }
    @Test
    void enterGuest() {
    }

    @Test
    void exitGuest() {
    }

    @Test
    void register() {
    }

    @Test
    void login() {
    }

    @Test
    void checkPassword() {
    }

    @Test
    void checkCriteria(){
        ConfigParser.getInstance("../../config1.json");
        int max = Dao.getMaxId("Message", "messageId");
        System.out.println(max);
    }
    @Test
    void checkBirthday() {
        StringChecks sc = new StringChecks();
        assertTrue(sc.checkBirthday("24/02/2002"));
        assertFalse(sc.checkBirthday("24/02/1902"));
        assertFalse(sc.checkBirthday("24/02/2024"));
        assertFalse(sc.checkBirthday("24/05/2060"));
        assertFalse(sc.checkBirthday("29/02/2022"));
        //System.out.println(hour + " " + minute +" " + second);
    }

    @Test
    void checkEmail() {
        StringChecks sc = new StringChecks();
        assertTrue(sc.checkEmail("eliben@gmail.com"));
        assertTrue(sc.checkEmail("eliben1233@gmail.com"));
        assertTrue(sc.checkEmail("benshime@post.bgu.ac.il"));
        assertFalse(sc.checkEmail("elibengmail.com"));
        assertFalse(sc.checkEmail("eliben@gmailcom"));
        assertFalse(sc.checkEmail("eliben@gmail"));
        assertFalse(sc.checkEmail("eliben@gmail.c"));

    }
}