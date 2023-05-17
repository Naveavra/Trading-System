package domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {
    private Guest guest;

    @BeforeEach
    void setUp() {
        guest = new Guest(2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProductToCart_success() {
        try {
            guest.addProductToCart(1, 0, 100);
            HashMap<Integer, HashMap<Integer, Integer>> cart = guest.getCartContent();
            assertTrue(cart.get(1) != null);
            HashMap<Integer, Integer> products = new HashMap<>();
            products = cart.get(1);
            assertTrue(products.get(0) == 100);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void addProductToCart_fail() {
        try {
            guest.addProductToCart(1, 0, -100);
            assertTrue(false);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void removeProductFromCart_success() {
        try{
            guest.addProductToCart(1, 0, 100);
            guest.removeProductFromCart(1,0);
            HashMap<Integer, HashMap<Integer, Integer>> cart = guest.getCartContent();
            assertTrue(cart.get(1)==null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void removeProductFromCart_fail() {
        try{
            guest.removeProductFromCart(0,0);
            assertTrue(false);
        }catch (Exception e){
            assertTrue(true);
        }
    }
    @Test
    void changeQuantityInCart_success() {
        try{
            guest.addProductToCart(1, 0, 100);
            guest.addQuantityInCart(1,0,10);
            HashMap<Integer, HashMap<Integer, Integer>> cart = guest.getCartContent();
            assertTrue(cart.get(1)!=null);
            assertTrue(cart.get(1).get(0)==110);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void changeQuantityInCart_fail() {
        try{
            guest.addProductToCart(0,0,-10);
            assertFalse(true);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void getCartContent_success() {
        try{
            guest.addProductToCart(1, 0, 100);
            HashMap<Integer, HashMap<Integer, Integer>> cart = guest.getCartContent();
            assertTrue(cart.get(1)!=null);
            assertTrue(cart.get(1).get(0)==100);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void getCartContent_fail() {
        try{
            guest.addProductToCart(1, 0, 100);
            HashMap<Integer, HashMap<Integer, Integer>> cart = guest.getCartContent();
            assertFalse(cart.get(2)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }


    @Test
    void emptyCart_success() {
        try{
            guest.addProductToCart(1, 0, 100);
            guest.emptyCart();
            HashMap<Integer, HashMap<Integer, Integer>> cart = guest.getCartContent();
            assertFalse(cart.get(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void emptyCart_fail() {
        try{
            guest.addProductToCart(1, 0, 100);
            guest.emptyCart();
            HashMap<Integer, HashMap<Integer, Integer>> cart = guest.getCartContent();
            assertFalse(cart.get(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
}