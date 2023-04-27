package domain.user;

import domain.states.StoreCreator;
import domain.states.StoreManager;
import domain.states.StoreOwner;
import domain.states.UserState;
import domain.store.storeManagement.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.messageRelated.Message;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    private Member m;
    private Store s;
    private LinkedList<String> answers;
    @BeforeEach
    void setUp() {
        m = new Member(1, "ziv@gmail.com", "ziv1234", "22/04/2002");
        s = new Store(0, "", 1);
        answers = new LinkedList<>();
        try {
            //s.appointUser(1, 1, Role.Manager);
        } catch (Exception e) {

        }
    }
    @Test
    void connect() {
            m.connect();
            assertTrue(m.getIsConnected());
    }

    @Test
    void disconnect() {
        m.disconnect();
        assertFalse(m.getIsConnected());
    }

    @Test
    void changeRoleInStore_success() {
        try{
            m.login("ziv1234", answers);
            UserState u = new StoreCreator();
            m.changeRoleInStore(0, u,s);
            assertTrue(m.checkPermission(Action.appointOwner,0));
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void changeRoleInStore_fail() {
        try{
            m.login("ziv1234", answers);
            UserState u =new StoreManager();
            m.changeRoleInStore(0, u,s);
            assertFalse(m.checkPermission(Action.appointOwner,0));
        }catch (Exception e){
            assertTrue(false);
        }
    }


    @Test
    void login_success() {
        try {
            LinkedList<String> asnwers = new LinkedList<>();
            m.login("ziv1234", asnwers);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);

        }
    }

    @Test
    void login_fail() {
        try {
            m.login("ziv123", answers);
            assertTrue(false);
        } catch (Exception e) {
            assertEquals("wrong password", e.getMessage());
        }
    }


    @Test
    void purchaseMade() {
        try{
        m.addProductToCart(0,1,100);
        m.purchaseMade(0,100);
            HashMap<Integer, HashMap<Integer, Integer>> cart=  m.getCartContent();
            assertTrue(cart.size()==0);
            String s ="{\"0\":{\"0\":{\"1\":100}}}";
            assertEquals(s,m.getUserPurchaseHistory());
    }catch (Exception e){
        assertTrue(false);
        }
    }

    @Test
    void openStore() {
        try{
        m.login("ziv1234", answers);
        m.openStore(s);
        assertTrue(m.checkPermission(Action.appointOwner, 0));
    }catch (Exception e) {
        }
        }

    @Test
    void writeReview() {
        try{
            m.login("ziv1234",answers );
            m.addProductToCart(0,1,100);
            m.purchaseMade(0,100);
          Message message =  m.writeReview(0,0,0,"good review",2);
            assertTrue(message.getOrderId()==0);
            assertTrue(message.getRating()==2);
            assertEquals("good review",message.getContent());
    }catch (Exception e) {
        System.out.println(e.getMessage());
        }
        }
    @Test
    void writeReview_fail() {
        try{
            m.login("ziv1234",answers );
            m.addProductToCart(0,1,100);
            m.purchaseMade(0,100);
            Message message =  m.writeReview(0,0,1,"good review",2);
        }catch (Exception e) {
            assertEquals("can't write a review for an order that didn't occur",e.getMessage());
        }
    }

    @Test
    void writeComplaint() {
        try {
            m.login("ziv1234",answers );
            m.addProductToCart(0, 1, 100);
            m.purchaseMade(0, 100);
            Message message = m.writeComplaint(0,0,0,"u sacks!!");
            assertTrue(message.getOrderId()==0);
            assertEquals("u sacks!!",message.getContent());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
    @Test
    void writeComplaint_fail() {
        try{
            m.login("ziv1234",answers );
            m.addProductToCart(0,1,100);
            m.purchaseMade(0,100);
            Message message =  m.writeComplaint(0,0,1,"u sacks!!");
        }catch (Exception e) {
            assertEquals("can't write a review because the store wasn't part of the order",e.getMessage());
        }
    }
    @Test
    void sendQuestion() {
        try {
            m.login("ziv1234", answers);
            Message message = m.sendQuestion(0, 0, "how r u");
            assertEquals(message.getContent(), "how r u");
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void addNotification() {
    }

    @Test
    void displayNotifications() {
    }

    @Test
    void canCheckMessages() {
    }

    @Test
    void canGiveFeedback() {
    }

    @Test
    void getUserPurchaseHistory() {
    }

    @Test
    void getPrivateInformation() {
    }

    @Test
    void addQuestionForLogin() {
    }

    @Test
    void changeAnswerForQuestion() {
    }

    @Test
    void removeSecurityQuestion() {
    }

    @Test
    void appointToManager() {
    }

    @Test
    void appointToOwner() {
    }

    @Test
    void checkRoleInStore() {
    }

    @Test
    void fireOwner() {
    }

    @Test
    void removeRoleInStore() {
    }

    @Test
    void fireManager() {
    }

    @Test
    void addAction() {
    }

    @Test
    void removeAction() {
    }

    @Test
    void closeStore() {
    }

    @Test
    void reOpenStore() {
    }

    @Test
    void checkPermission() {
    }

    @Test
    void changeToInactive() {
    }

    @Test
    void changeToActive() {
    }

    @Test
    void getWorkerIds() {
    }

    @Test
    void getInformation() {
    }

    @Test
    void getAllStoreIds() {
    }
}