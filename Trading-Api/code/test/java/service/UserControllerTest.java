package service;

import domain.store.storeManagement.Store;
import domain.user.StringChecks;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

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
    void fireManager(){
        UserController us = new UserController();
        try {
            us.register(1, "eli@gmail.com", "123Aaa", "aaaaaa", "24/02/2002");
            us.register(2, "eli2@gmail.com", "123Aaa", "aaaaaa", "24/02/2002");
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