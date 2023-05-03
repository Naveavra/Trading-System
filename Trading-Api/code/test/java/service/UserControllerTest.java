package service;

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
    void checkBirthday() {
        UserController us = new UserController();
        assertTrue(us.checkBirthday("24/02/2002"));
        assertFalse(us.checkBirthday("24/02/1902"));
        assertFalse(us.checkBirthday("24/02/2024"));
        assertFalse(us.checkBirthday("24/05/2023"));
        assertFalse(us.checkBirthday("28/04/2023"));
        assertFalse(us.checkBirthday("29/02/2022"));
        //System.out.println(hour + " " + minute +" " + second);
    }

    @Test
    void checkEmail() {
        UserController us = new UserController();
        assertTrue(us.checkEmail("eliben@gmail.com"));
        assertTrue(us.checkEmail("eliben1233@gmail.com"));
        assertTrue(us.checkEmail("benshime@post.bgu.ac.il"));
        assertFalse(us.checkEmail("elibengmail.com"));
        assertFalse(us.checkEmail("eliben@gmailcom"));
        assertFalse(us.checkEmail("eliben@gmail"));
        assertFalse(us.checkEmail("eliben@gmail.c"));

    }
}