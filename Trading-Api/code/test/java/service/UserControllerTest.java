package service;

import database.dtos.MemberDto;
import database.dtos.NotificationDto;
import database.dtos.ReceiptDto;
import database.dtos.UserHistoryDto;
import domain.store.product.Product;
import domain.store.storeManagement.Store;
import domain.user.Member;
import domain.user.StringChecks;
import market.Admin;
import org.junit.jupiter.api.Test;
import utils.infoRelated.ProductInfo;
import utils.messageRelated.Notification;
import utils.messageRelated.NotificationOpcode;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

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
        UserController us = new UserController();
        try {
            us.register("eli@gmail.com", "123Aaa", "aaabbbbbaa", "24/02/2002");
            us.register("eli2@gmail.com", "123Aaa", "aaaaaccca", "24/02/2002");
            int id = us.login("eli@gmail.com", "aaabbbbbaa");
            us.addNotification(id, new Notification(NotificationOpcode.CHAT_MESSAGE, "test"));
            Admin a = new Admin(1, "elibs@gmail.com", "123Aaa");
            us.addAdmin(a, "aaaaaa");
            Member member = us.getMember(2);
            Store s = new Store(0, "", member);
            Store store = new Store(1, "nike", "good store","",member);
            us.openStore(2,s);
            us.openStore(2,store);
            AtomicInteger aid = new AtomicInteger(1);
            s.addNewProduct("apple", "pink apple", aid, 5, 3);
            s.addNewProduct("banana", "yellow banana", aid, 6, 4);
            Product apple = s.getInventory().getProduct(1);
            ProductInfo p =  new ProductInfo(0, apple, 10);
            Product banana = s.getInventory().getProduct(2);
            ProductInfo p2 =  new ProductInfo(0, banana, 10);
            us.addProductToCart(2, 0, p, 4);
            us.addProductToCart(2, 0, p2, 5);
            us.purchaseMade(2, 0, 30);
            us.updateAdminState(1);
            us.updateMemberState(2);
            us.saveMemberState(3);

            MemberDto m = us.getMemberDto(2);
            System.out.println(m.getEmail());
            List<NotificationDto> nlist = us.getSubscriberNotificationsDto(2);
            for(NotificationDto n : nlist)
                System.out.println(n.getId() + " " + n.getContent());
            List<UserHistoryDto> hlist = us.getMemberUserHistoryDto(2);
            for(UserHistoryDto history : hlist) {
                System.out.println(history.getReceiptId() + " " + history.getTotalPrice());
                for(ReceiptDto r : history.getReceiptDtos())
                    System.out.println(r.getStoreId() + " " + r.getProductId() + " " + r.getQuantity());
            }
            assert true;
        }catch (Exception e){
            assert false;
        }
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