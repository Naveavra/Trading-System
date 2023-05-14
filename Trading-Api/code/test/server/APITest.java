package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class APITest {
    private HashMap<Integer, HashMap<Integer, Integer>> cart;
    private API api;
    @BeforeEach
    void setUp() {
        api = new API();
        cart = new HashMap<>();
        cart.put(1, new HashMap<>());
        cart.get(1).put(1, 1);
        cart.put(5, new HashMap<>());
        cart.get(5).put(5, 5);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBaskets() {
        System.out.println(api.getBaskets(cart));
    }
}