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
    void checkDatabase(){
        Admin a = new Admin(1, "elibs@gmail.com", "123Aaa");
        Market market = new Market(a);
        try {
            market.register("eli@gmail.com", "123Aaa",  "24/02/2002");
            market.register("eli2@gmail.com", "123Aaa", "24/02/2002");
            LoginInformation loginInformation = market.login("eli@gmail.com", "123Aaa").getValue();
            int id = loginInformation.getUserId();
            String token = loginInformation.getToken();
            loginInformation = market.login("eli2@gmail.com", "123Aaa").getValue();
            int id2 = loginInformation.getUserId();
            String token2 = loginInformation.getToken();
            market.addNotification(id, NotificationOpcode.GET_CLIENT_DATA, "test");

            int sid = market.openStore(id,token, "nike", "good store", "da;nen").getValue();
            int sid2 = market.openStore(id2,token2, "adidas", "bad store", "dkndn").getValue();
            market.appointManager(id, token, "eli2@gmail.com", sid);
            List<String> categories = new ArrayList<>();
            categories.add("good");
            int pid = market.addProduct(id, token, sid, categories, "apple", "pink apple", 10, 20, "iifii").getValue();
            categories.add("bad");
            int pid2 = market.addProduct(id, token, sid, categories, "banana", "yellow banana", 7, 15, "ffjfjffii").getValue();
            market.updateProduct(id, token, sid,pid2, categories, "null", "green banana", -1, -1, "null");
            market.addProductToCart(id, sid, pid, 3);
            market.addProductToCart(id, sid, pid2, 5);
            Receipt receipt = market.makePurchase(id, payment, supplier).getValue();
            market.addProductToCart(id, sid, pid, 2);
            market.addProductToCart(id, sid, pid2, 3);
            int gid = market.enterGuest().getValue();
            market.addProductToCart(gid, sid, pid2, 3);
            assertNotNull(receipt);
            market.writeReviewToStore(id, token, receipt.getOrderId(), "nike", "very good", 4);
            market.writeReviewToProduct(id, token, receipt.getOrderId(), sid, pid, "very good", 4);
            market.sendComplaint(id, token, receipt.getOrderId(), "baaaad?");
            market.sendQuestion(id, token, sid2, "is open at 8?");
            assert true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    void checkNewImp(){
        Admin a = new Admin(1, "elibenshimol6@gmail.com", "123Aaa");
        Market market = new Market(a);
        try {
            market.register("eli@gmail.com", "123Aaa", "24/02/2002");
            market.register("chai@gmail.com", "123Aaa", "01/01/2002");
            market.register("miki@gmail.com", "123Aaa", "01/01/2002");

            LoginInformation log = market.login("eli@gmail.com", "123Aaa").getValue();
            LoginInformation log2 = market.login("elibenshimol6@gmail.com", "123Aaa").getValue();
            LoginInformation log3 = market.login("chai@gmail.com", "123Aaa").getValue();
            LoginInformation log4 = market.login("miki@gmail.com", "123Aaa").getValue();

            int sid = market.openStore(log.getUserId(), log.getToken(), "nike", "good store", "img").getValue();
            List<String> categories = new ArrayList<>();
            categories.add("good");
            categories.add("yumi");
            int pid = market.addProduct(log.getUserId(), log.getToken(), sid, categories, "boot",
                    "good one", 100, 20, "img").getValue();

            market.addProductToCart(log.getUserId(), sid, pid, 4);
            int oid = market.makePurchase(log.getUserId(), null, null).getValue().getOrderId();
            market.sendComplaint(log.getUserId(), log.getToken(), oid, "the products were bad");
            market.sendQuestion(log.getUserId(), log.getToken(), sid, "is open at 8?");
            market.writeReviewToStore(log.getUserId(), log.getToken(), oid, "nike", "good store", 4);
            market.writeReviewToProduct(log.getUserId(), log.getToken(), oid, sid, pid, "good store", 4);

            market.addProductToCart(log.getUserId(), sid, pid, 6);
            market.changeQuantityInCart(log.getUserId(), sid, pid, 2);

            market.addNotification(log.getUserId(), NotificationOpcode.GET_CLIENT_DATA, "test");
            market.addNotification(log2.getUserId(), NotificationOpcode.GET_CLIENT_DATA, "test");
            market.displayNotifications(log.getUserId(), log.getToken());

            market.appointOwner(log.getUserId(), log.getToken(), "chai@gmail.com", sid);
            market.appointManager(log3.getUserId(), log3.getToken(), "miki@gmail.com", sid);

            List<Action> addActions = new ArrayList<>();
            addActions.add(Action.addProduct);
            addActions.add(Action.removeProduct);
            List<Integer> addIds = new ArrayList<>();
            for(Action action : addActions)
                addIds.add(Permissions.actionsMap.get(action));
            market.addManagerPermissions(log.getUserId(), log.getToken(), log4.getUserId(), sid, addIds);
            addActions.remove(Action.removeProduct);
            addIds.clear();
            for(Action action : addActions)
                addIds.add(Permissions.actionsMap.get(action));
            market.removeManagerPermissions(log.getUserId(), log.getToken(), log4.getUserId(), sid, addIds);

//            market.answerComplaint(log2.getUserId(), log2.getToken(), 0, "sent new products");
            market.answerQuestion(log.getUserId(), log.getToken(), sid, 1, "yes");

            //market.fireOwner(log.getUserId(), log.getToken(), log4.getUserId(), sid);
            market.displayNotifications(log2.getUserId(), log2.getToken());
            //market.displayNotifications(log4.getUserId(), log4.getToken());

//            market.deleteProduct(log.getUserId(), log.getToken(), sid, pid);
        }catch (Exception e){
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    void checkImp(){
       UserController userController = new UserController();
        MarketController marketController = new MarketController();
       try {
           Member m = userController.getMember(3);
           assertEquals(m.getId(), 3);
           Admin a = userController.getAdmin("elibenshimol6@gmail.com");
           userController.login("elibenshimol6@gmail.com", a.getPassword());
           //userController.answerComplaint(a.getId(), 0, "sent new products");
           Store s = marketController.getStore(0);
           AppHistory appHistory = s.getAppHistoryFromDb();
           assertEquals(appHistory.getRoles().size(), 3);
       }catch (Exception e){
           System.out.println(e.getMessage());
           assert false;
       }
    }

    @Test
    void checkNotifications(){
        Admin a = new Admin(1, "elibenshimol6@gmail.com", "123Aaa");
        Market market = new Market(a);
        market.register("eli@gmail.com", "123Aaa", "24/02/2002");
        market.register("chai@gmail.com", "123Aaa", "01/01/2002");
        int id = market.login("eli@gmail.com", "123Aaa").getValue().getUserId();
        int id2 = market.login("chai@gmail.com", "123Aaa").getValue().getUserId();
        market.logout(id2);
        market.sendNotification(id, market.addTokenForTests(), NotificationOpcode.GET_CLIENT_DATA, "chai@gmail.com", "hi");
        LoginInformation log = market.login("chai@gmail.com", "123Aaa").getValue();
        for(Notification n : log.getNotifications())
            System.out.println(n.toString());
        assertEquals(1, log.getNotifications().size());
    }


    @Test
    void fireManager(){
        UserController us = new UserController();
        try {
            us.register("eli@gmail.com", "123Aaa", "aaaaaa", "24/02/2002");
            us.register( "eli2@gmail.com", "123Aaa", "aaaaaa", "24/02/2002");
            us.login("eli@gmail.com", "aaaaaa");
            us.openStore(1, new Store(0, "nike", "good store", "img", us.getMember(1)));
            us.appointManager(1, "eli2@gmail.com", 0);
            System.out.println(us.getMember(2).getRole(0).getRole());
            us.fireManager(1, 2, 0);
            System.out.println(us.getMember(2).getRole(0).getRole());
            assert false;
        }catch (Exception e){
            assert true;
        }
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