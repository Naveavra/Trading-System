package domain.user;

import database.daos.Dao;
import domain.states.StoreCreator;
import domain.states.StoreManager;
import domain.states.StoreOwner;
import domain.states.UserState;
import domain.store.product.Product;
import domain.store.storeManagement.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Pair;
import utils.infoRelated.ProductInfo;
import utils.infoRelated.Receipt;
import utils.messageRelated.*;
import utils.stateRelated.Action;
import utils.stateRelated.Role;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemberTest {
    private Member m;
    private Store s;

    private Product apple;
    private ProductInfo p;
    private Product banana;
    private ProductInfo p2;

    private LinkedList<String> answers;
    @BeforeEach
    void setUp(){
        Dao.setForTests(true);
        m = new Member(2, "ziv@gmail.com", "ziv1234", "22/04/2002");
        s = new Store(0, "", m);
        try {
            List<String> categories = new ArrayList<>();
            s.addNewProduct("apple", "pink apple", new AtomicInteger(1), 5, 3, categories,null);
            apple = s.getInventory().getProduct(1);
            p =  new ProductInfo(s.getStoreId(), apple, 10);
        }catch (Exception e){
            assert false;
        }
        answers = new LinkedList<>();
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
            m.login("ziv1234");
            UserState u = new StoreCreator(m.getId(), m.getName(), s);
            m.changeRoleInStore(u,s,null);
            m.checkPermission(Action.appointOwner,s.getStoreId());
            assert true;
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void changeRoleInStore_fail() {
        try{
            m.login("ziv1234");
            UserState u =new StoreManager(m.getId(), m.getName(),s);
            m.changeRoleInStore(u,s,null);
            m.checkPermission(Action.appointOwner,s.getStoreId());
            assert false;
        }catch (Exception e){
            assert true;
        }
    }


    @Test
    void login_success() {
        try {
            m.login("ziv1234");
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);

        }
    }

    @Test
    void login_fail() {
        try {
            m.login("ziv123");
            assertTrue(false);
        } catch (Exception e) {
            assertEquals("wrong password", e.getMessage());
        }
    }


    @Test
    void purchaseMade() {
        try{
            m.login("ziv1234");
            m.addProductToCart(s.getStoreId(),p,100,null);
            m.purchaseMade(new Receipt( 0, new ShoppingCart(m.getShoppingCart()), 100),null);
            ShoppingCart cart = m.getShoppingCart();
            assertEquals(0, cart.getContent().size());
            PurchaseHistory history = m.getUserPurchaseHistory();
            assertEquals(history.getHisSize(), 1);
            for(Receipt receipt : history.getPurchaseHistory().values()) {
                ShoppingCart his = history.getPurchaseHistory().get(receipt.getOrderId()).getCart();
                for (ProductInfo product : his.getContent())
                    assertTrue(p.id == product.id && product.quantity == 100);
        }
        }catch (Exception e){
            assert false;
        }
    }

    @Test
    void openStore() {
        try{
        m.login("ziv1234");
        m.openStore(s,null);
        m.checkPermission(Action.appointOwner, s.getStoreId());
        assert true;
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void writeReview() {
        try{
            m.login("ziv1234" );
            m.addProductToCart(s.getStoreId(),p,100,null);
            m.purchaseMade(new Receipt(0, new ShoppingCart(m.getShoppingCart()), 100),null);
            StoreReview message =  m.writeReview(0, s.getStoreId(),p.id,0,"good review",2);
            assertEquals(0, message.getOrderId());
            assertEquals(2, message.getRating());
            assertEquals("good review",message.getContent());
         }catch (Exception e) {
        System.out.println(e.getMessage());
        assert false;
        }
    }
    @Test
    void writeReview_fail() {
        try{
            m.login("ziv1234" );
            m.addProductToCart(s.getStoreId(),p,100,null);
            m.purchaseMade(new Receipt(0, new ShoppingCart(m.getShoppingCart()), 100),null);
            Message message =  m.writeReview(s.getStoreId(),p.id,1,"good review",2);
        }catch (Exception e) {
            assert true;
        }
    }

    @Test
    void writeComplaint() {
        try {
            m.login("ziv1234");
            m.addProductToCart(s.getStoreId(), p, 100,null);
            m.purchaseMade(new Receipt( 0, new ShoppingCart(m.getShoppingCart()), 100),null);
            Complaint message = m.writeComplaint(0, 0,"u sacks!!");
            assertEquals(0, message.getOrderId());
            assertEquals("u sacks!!",message.getContent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    void writeComplaint_fail() {
        try{
            m.login("ziv1234" );
            m.addProductToCart(s.getStoreId(),p,100,null);
            m.purchaseMade(new Receipt( 0, new ShoppingCart(m.getShoppingCart()), 100),null);
            Message message =  m.writeComplaint(0, 0,"u sacks!!");
        }catch (Exception e) {
            assertEquals("can't write a review because the store wasn't part of the order",e.getMessage());
        }
    }
    @Test
    void sendQuestion() {
        try {
            m.login("ziv1234");
            Message message = m.sendQuestion(0, s.getStoreId(), "how r u");
            assertEquals(message.getContent(), "how r u");
        }catch (Exception e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void displayNotifications() {
        try {
            Notification n = new Notification(NotificationOpcode.GET_CLIENT_DATA_AND_STORE_DATA, "u have a new message");
            m.login("ziv1234");
            m.addNotification(n,null);
           List<Notification> notifiy =  m.displayNotifications();
           assertTrue(notifiy.size()==1);
           assertEquals(notifiy.get(0).toString(),"the value of the notification is: " + n.getNotification());
           assertTrue(m.displayNotifications().size()==0);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void canCheckMessages() {
        try{
            m.login("ziv1234");
            m.openStore(s,null);
            m.checkPermission(Action.viewMessages, s.getStoreId());
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            newMem1.login("Ziv12345");
            s.appointUser(m.getId(),newMem1, new StoreManager(newMem1.getId(), newMem1.getName(), s));
            StoreManager sm = new StoreManager(newMem1.getId(), m.getName(),s);
            newMem1.changeRoleInStore(sm, s,null);
            newMem1.checkPermission(Action.viewMessages,s.getStoreId());
            Member newMem2 = new Member(4, "ziv1@gmail.com","Ziv12345","1/1/2000");
            newMem2.login("Ziv12345");
            s.appointUser(m.getId(),newMem2, new StoreOwner(newMem2.getId(), newMem2.getName(), s));
            StoreOwner so = new StoreOwner(newMem2.getId(), m.getName(), s);
            newMem2.changeRoleInStore(so, s,null);
            newMem2.checkPermission(Action.viewMessages, s.getStoreId());
        }catch (Exception e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void canGiveFeedback() {
        try{
            m.login("ziv1234");
            m.openStore(s,null);
            m.checkPermission(Action.answerMessage, s.getStoreId());
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            newMem1.login("Ziv12345");
            s.appointUser(m.getId(),newMem1, new StoreManager(newMem1.getId(), newMem1.getName(), s));
            StoreManager sm = new StoreManager(newMem1.getId(), m.getName(), s);
            newMem1.changeRoleInStore(sm, s,null);
            newMem1.checkPermission(Action.answerMessage, s.getStoreId());
            Member newMem2 = new Member(4, "ziv1@gmail.com","Ziv12345","1/1/2000");
            newMem2.login("Ziv12345");
            s.appointUser(m.getId(),newMem2, new StoreManager(newMem2.getId(), newMem2.getName(), s));
            StoreOwner so = new StoreOwner(newMem2.getId(), m.getName(), s);
            newMem2.changeRoleInStore(so, s,null);
            newMem2.checkPermission(Action.answerMessage, s.getStoreId());
        }catch (Exception e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }


    @Test
    void appointToManager() {
        try{
            m.login("ziv1234");
            m.openStore(s,null);
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToManager(newMem1, s.getStoreId(),null);
            Pair<Member,UserState> pairs= s.getAppHistory().getNode(newMem1.getId()).getData();
            assertTrue(s.getUsersInStore().contains(newMem1.getId()));
            assertEquals(pairs.getSecond().getRole(),Role.Manager);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
        }

    @Test
    void appointToOwner() {
        try{
            m.login("ziv1234");
            m.openStore(s,null);
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToOwner(newMem1, s.getStoreId(),null);
            Pair<Member,UserState> pairs= s.getAppHistory().getNode(newMem1.getId()).getData();
            assertTrue(s.getUsersInStore().contains(newMem1.getId()));
            assertEquals(pairs.getSecond().getRole(),Role.Owner);
        }catch (Exception e) {
            System.out.println();
            assertTrue(false);
        }
    }

    @Test
    void checkRoleInStore() {
        try {
            m.openStore(s,null);
            assertEquals( m.getRole(s.getStoreId()).getRole() ,Role.Creator);
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToManager(newMem1, s.getStoreId(),null);
            newMem1.checkPermission(Action.viewMessages, s.getStoreId());
        }catch (Exception e){
            System.out.println(e.getMessage());
            assertTrue(false);

        }
    }

    @Test
    void fireOwner() {
        try {
            m.openStore(s,null);
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToOwner(newMem1, s.getStoreId(),null);
            m.fireOwner(newMem1.getId(), s.getStoreId(),null);
            assertNull(s.getAppHistory().getNode(3));
        }catch (Exception e){
        System.out.println(e.getMessage());
        assert false;
        }
    }

    @Test
    void removeRoleInStore() {
        try {
            m.openStore(s,null);
            m.removeRoleInStore(s.getStoreId(),null);
            m.getRole(s.getStoreId());
            assert false;
        }catch (Exception e){
            assert true;
        }
    }

    @Test
    void fireManager() {
        try{
            m.login("ziv1234");
            m.openStore(s,null);
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToManager(newMem1, s.getStoreId(),null);
            m.fireManager(newMem1.getId(),s.getStoreId(),null);
            assertFalse(s.getUsersInStore().contains(newMem1.getId()));
            assertNull(s.getAppHistory().getNode(newMem1.getId()));
        }catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    void addAction() {
        try{
            m.openStore(s,null);
            Member newMem1 = new Member(3, "ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToManager(newMem1, s.getStoreId(),null);
            StoreManager sm = new StoreManager(newMem1.getId(), m.getName(), s);
            List<Action> act = new LinkedList<>(sm.getActions());
            for(Action a : act){
                newMem1.removeAction(a,s.getStoreId(),null);
                newMem1.addAction(a,s.getStoreId(),null);
                newMem1.checkPermission(a,s.getStoreId());
                assert true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            assert false;
        }
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