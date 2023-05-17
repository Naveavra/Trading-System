package domain.user;

import domain.store.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    Guest g ;
    ShoppingCart s ;
    @BeforeEach
    void setUp() {
        g = new Guest(1);
        s = new ShoppingCart();
    }

    @Test
    void addProductToCart_success() {
        try {
            s.addProductToCart(1, 0, 100);
            HashMap<Integer, HashMap<Integer, Integer>> cart =  s.getContent();
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
            s.addProductToCart(1, 0, -100);
            assertTrue(false);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void removeProductFromCart_success() {
        try{
            s.addProductToCart(1, 0, 100);
            s.removeProductFromCart(1,0);
            HashMap<Integer, HashMap<Integer, Integer>> cart = s.getContent();
            assertTrue(cart.get(1)==null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void removeProductFromCart_fail() {
        try{
            s.removeProductFromCart(0,0);
            assertTrue(false);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void changeQuantityInCart_success() {
        try{
            s.addProductToCart(1, 0, 100);
            s.addQuantityInCart(1,0,10);
            HashMap<Integer, HashMap<Integer, Integer>> cart = s.getContent();
            assertTrue(cart.get(1)!=null);
            assertTrue(cart.get(1).get(0)==110);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void changeQuantityInCart_fail() {
        try{
            s.addQuantityInCart(0,0,-10);
            assertFalse(true);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void getContent_success() {
        try{
            s.addProductToCart(1, 0, 100);
            HashMap<Integer, HashMap<Integer, Integer>> cart = s.getContent();
            assertTrue(cart.get(1)!=null);
            assertTrue(cart.get(1).get(0)==100);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void getContent_fail() {
        try{
            s.addProductToCart(1, 0, 100);
            HashMap<Integer, HashMap<Integer, Integer>> cart = s.getContent();
            assertFalse(cart.get(2)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void emptyCart_success() {
        try{
            s.addProductToCart(1, 0, 100);
            s.emptyCart();
            HashMap<Integer, HashMap<Integer, Integer>> cart = s.getContent();
            assertFalse(cart.get(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void emptyCart_fail() {
        try{
            s.addProductToCart(1, 0, 100);
            s.emptyCart();
            HashMap<Integer, HashMap<Integer, Integer>> cart = s.getContent();
            assertFalse(cart.get(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
}