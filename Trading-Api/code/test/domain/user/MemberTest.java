package domain.user;

import com.google.gson.Gson;
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
import utils.messageRelated.Message;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;
import utils.stateRelated.Action;
import utils.stateRelated.Role;
import utils.infoRelated.Info;

import java.util.HashMap;
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
        m = new Member(1, "ziv@gmail.com", "ziv1234", "22/04/2002");
        s = new Store(0, "", m);
        try {
            s.addNewProduct("apple", "pink apple", new AtomicInteger(1), 5, 3);
            apple = s.getInventory().getProduct(1);
            p =  new ProductInfo(0, apple, 10);
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
            m.changeRoleInStore(u,s);
            m.checkPermission(Action.appointOwner,0);
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
            m.changeRoleInStore(u,s);
            m.checkPermission(Action.appointOwner,0);
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
            m.addProductToCart(0,p,100);
            m.purchaseMade(0,100);
            ShoppingCart cart = m.getShoppingCart();
            assertEquals(0, cart.getContent().size());
            PurchaseHistory history = m.getUserPurchaseHistory();
            assertEquals(history.getHisSize(), 1);
            for(int orderId : history.getPurchaseHistory().keySet()) {
                ShoppingCart his = history.getPurchaseHistory().get(orderId);
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
        m.openStore(s);
        m.checkPermission(Action.appointOwner, 0);
        assert true;
        }catch (Exception e) {
            assert false;
        }
    }

    @Test
    void writeReview() {
        try{
            m.login("ziv1234" );
            m.addProductToCart(0,p,100);
            m.purchaseMade(0,100);
            Message message =  m.writeReview(0,0,0,"good review",2);
            assertTrue(message.getOrderId()==0);
            assertTrue(message.getRating()==2);
            assertEquals("good review",message.getContent());
         }catch (Exception e) {
        System.out.println(e.getMessage());
        assertTrue(false);
        }
    }
    @Test
    void writeReview_fail() {
        try{
            m.login("ziv1234" );
            m.addProductToCart(0,p,100);
            m.purchaseMade(0,100);
            Message message =  m.writeReview(0,0,1,"good review",2);
        }catch (Exception e) {
            assert true;
        }
    }

    @Test
    void writeComplaint() {
        try {
            m.login("ziv1234");
            m.addProductToCart(0, p, 100);
            m.purchaseMade(0, 100);
            Message message = m.writeComplaint(0,0,0,"u sacks!!");
            assertTrue(message.getOrderId()==0);
            assertEquals("u sacks!!",message.getContent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }
    @Test
    void writeComplaint_fail() {
        try{
            m.login("ziv1234" );
            m.addProductToCart(0,p,100);
            m.purchaseMade(0,100);
            Message message =  m.writeComplaint(0,0,1,"u sacks!!");
        }catch (Exception e) {
            assertEquals("can't write a review because the store wasn't part of the order",e.getMessage());
        }
    }
    @Test
    void sendQuestion() {
        try {
            m.login("ziv1234");
            Message message = m.sendQuestion(0, 0, "how r u");
            assertEquals(message.getContent(), "how r u");
        }catch (Exception e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void displayNotifications() {
        try {
            Notification<String> n = new Notification<>(NotificationOpcode.CLOSE_STORE, "u have a new message");
            m.login("ziv1234");
            m.addNotification(n);
           List<String> notifiy =  m.displayNotifications();
           assertTrue(notifiy.size()==1);
           assertEquals(notifiy.get(0),"the value of the notification is: "+n.getNotification());
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
            m.openStore(s);
            m.checkPermission(Action.viewMessages, s.getStoreId());
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            newMem1.login("Ziv12345");
            s.appointUser(m.getId(),newMem1, new StoreManager(newMem1.getId(), newMem1.getName(), s));
            StoreManager sm = new StoreManager(newMem1.getId(), m.getName(),s);
            newMem1.changeRoleInStore(sm, s);
            newMem1.checkPermission(Action.viewMessages,s.getStoreId());
            Member newMem2 = new Member(5,"ziv1@gmail.com","Ziv12345","1/1/2000");
            newMem2.login("Ziv12345");
            s.appointUser(m.getId(),newMem2, new StoreOwner(newMem2.getId(), newMem2.getName(), s));
            StoreOwner so = new StoreOwner(newMem2.getId(), m.getName(), s);
            newMem2.changeRoleInStore(so, s);
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
            m.openStore(s);
            m.checkPermission(Action.answerMessage, s.getStoreId());
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            newMem1.login("Ziv12345");
            s.appointUser(m.getId(),newMem1, new StoreManager(newMem1.getId(), newMem1.getName(), s));
            StoreManager sm = new StoreManager(newMem1.getId(), m.getName(), s);
            newMem1.changeRoleInStore(sm, s);
            newMem1.checkPermission(Action.answerMessage, s.getStoreId());
            Member newMem2 = new Member(5,"ziv1@gmail.com","Ziv12345","1/1/2000");
            newMem2.login("Ziv12345");
            s.appointUser(m.getId(),newMem2, new StoreManager(newMem2.getId(), newMem2.getName(), s));
            StoreOwner so = new StoreOwner(newMem2.getId(), m.getName(), s);
            newMem2.changeRoleInStore(so, s);
            newMem2.checkPermission(Action.answerMessage, s.getStoreId());
        }catch (Exception e){
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }


    @Test
    void getPrivateInformation() {
        try {
            m.login("ziv1234");
            Info res = m.getPrivateInformation();
            assertEquals(22,res.getAge());
            assertEquals("22/04/2002",res.getBirthday());
            assertEquals("ziv@gmail.com",res.getEmail());
            assertEquals("ziv",res.getName());

        }catch (Exception e){
        System.out.println(e.getMessage());
        assertTrue(false);
        }
    }

    @Test
    void appointToManager() {
        try{
            m.login("ziv1234");
            m.openStore(s);
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            Store store =  m.appointToManager(newMem1, 0);
            Pair<Member,UserState> pairs= store.getAppHistory().getNode(3).getData();
            assertTrue(store.getUsersInStore().contains(3));
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
            m.openStore(s);
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            Store store =  m.appointToOwner(newMem1, 0);
            Pair<Member,UserState> pairs= store.getAppHistory().getNode(3).getData();
            assertTrue(store.getUsersInStore().contains(3));
            assertEquals(pairs.getSecond().getRole(),Role.Owner);
        }catch (Exception e) {
            System.out.println();
            assertTrue(false);
        }
    }

    @Test
    void checkRoleInStore() {
        try {
            m.openStore(s);
            assertEquals( m.getRole(0).getRole() ,Role.Creator);
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToManager(newMem1, 0);
            newMem1.changeRoleInStore(new StoreManager(newMem1.getId(), m.getName(), s),s);
            newMem1.checkPermission(Action.viewMessages, s.getStoreId());
        }catch (Exception e){
            System.out.println(e.getMessage());
            assertTrue(false);

        }
    }

    @Test
    void fireOwner() {
        try {
            m.openStore(s);
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            Store store =  m.appointToOwner(newMem1, 0);
            m.fireOwner(3, 0);
           assertEquals(store.getAppHistory().getNode(3),null);
        }catch (Exception e){
        System.out.println(e.getMessage());
        assertTrue(false);
        }
    }

    @Test
    void removeRoleInStore() {
        try {
            m.openStore(s);
            m.removeRoleInStore(0);
            m.getRole(0);
            assert false;
        }catch (Exception e){
            assert true;
        }
    }

    @Test
    void fireManager() {
        try{
            m.login("ziv1234");
            m.openStore(s);
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            Store store =  m.appointToManager(newMem1, 0);
            m.fireManager(3,0);
            assertTrue(!store.getUsersInStore().contains(2));
            assertEquals(store.getAppHistory().getNode(2 ),null);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }

    @Test
    void addAction() {
        try{
            m.openStore(s);
            Member newMem1 = new Member(3,"ziv0@gmail.com","Ziv12345","1/1/2000");
            m.appointToManager(newMem1, s.getStoreId());
            StoreManager sm = new StoreManager(newMem1.getId(), m.getName(), s);
            newMem1.changeRoleInStore(sm, s);
            List<Action> act = new LinkedList<>(sm.getActions());
            for(Action a : act){
                newMem1.removeAction(a,s.getStoreId());
                newMem1.addAction(a,s.getStoreId());
                newMem1.checkPermission(a,0);
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