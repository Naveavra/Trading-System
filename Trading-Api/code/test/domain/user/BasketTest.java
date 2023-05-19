package domain.user;

import domain.store.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BasketTest {
    private Basket basket;

    @BeforeEach
    void setUp() {
        basket = new Basket(0);
    }

    //product - int prod_id, String _name, String desc
    @AfterEach
    void tearDown() {
    }

    @Test
    void addProductToCart_success() {
        try {
            Product apple = new Product(0, "apple", "red apple");
            Product banana = new Product(1, "banana", "yellow banana");
            basket.addProductToCart(0, 10);
            basket.addProductToCart(1, 10);
            HashMap<Integer, Integer> products = basket.getContent();
            assertTrue(products.get(0) != null);
            assertTrue(products.get(0) == 10);
            assertTrue(products.keySet().size() == 2);
        }catch (Exception e){
            assert false;
        }
    }

    @Test
    void addProductToCart_failed() {
        Product apple = new Product(0, "apple", "red apple");
        try {
            basket.changeQuantityInCart(0, -1);
            assertFalse(true);
        } catch (Exception e) {
            assert true;
        }
    }


    @Test
    void removeProductFromCart() {
        try {
            Product apple = new Product(0, "apple", "red apple");
            Product banana = new Product(1, "banana", "yellow banana");
            basket.addProductToCart(0, 10);
            basket.addProductToCart(1, 10);
            basket.removeProductFromCart(0);
            HashMap<Integer, Integer> products = basket.getContent();
            assertTrue(products.get(0) == null);
            assertTrue(products.keySet().size() == 1);
        }catch (Exception e){
            assert false;
        }
    }

    @Test
    void changeQuantityInCart() {
        try {
            Product apple = new Product(0,"apple","red apple");
            basket.addProductToCart(0,10);
            basket.changeQuantityInCart(0, 100);
            HashMap<Integer, Integer> products = basket.getContent();
            assertTrue(products.get(0) != null);
            assertTrue(products.get(0) == 110);
        }catch (Exception e){
            assert false;
        }
    }
}