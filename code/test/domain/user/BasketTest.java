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
        basket = new Basket();
    }
//product - int prod_id, String _name, String desc
    @AfterEach
    void tearDown() {
    }

    @Test
    void addProductToCart_success() {
        Product apple = new Product(0,"apple","red apple");
        Product banana = new Product(1,"banana","yellow banana");
        basket.addProductToCart(0,10);
        basket.addProductToCart(1,10);
        HashMap<Integer,Integer> products = basket.getContent();
        assertTrue(products.get(0)!=null);
        assertTrue(products.get(0)==10);
        assertTrue(products.keySet().size()==2);
    }
    @Test
    void addProductToCart_failed() {
        Product apple = new Product(0,"apple","red apple");
        try {
            basket.addProductToCart(0, -1);
            assertFalse(true);
        }catch (Exception e){
            assertEquals(e.getMessage(), "quantity mast be bigger then 0");
        }
    }


    @Test
    void removeProductFromCart_suucess() {
        Product apple = new Product(0,"apple","red apple");
        Product banana = new Product(1,"banana","yellow banana");
        basket.addProductToCart(0,10);
        basket.addProductToCart(1,10);
        basket.removeProductFromCart(0);
        HashMap<Integer,Integer> products = basket.getContent();
        assertTrue(products.get(0)==null);
        assertTrue(products.keySet().size()==1);
    }
    @Test
    void removeProductFromCart_fail() {
        Product apple = new Product(0,"apple","red apple");
        Product banana = new Product(1,"banana","yellow banana");
        basket.addProductToCart(0,10);

        basket.removeProductFromCart(0);
        HashMap<Integer,Integer> products = basket.getContent();
        assertTrue(products.get(0)==null);
        assertTrue(products.keySet().size()==1);
    }

    @Test
    void changeQuantityInCart() {
        Product apple = new Product(0,"apple","red apple");
        basket.addProductToCart(0,10);
        basket.changeQuantityInCart(0,100);
        HashMap<Integer,Integer> products = basket.getContent();
        assertTrue(products.get(0)!=null);
        assertTrue(products.get(0)==110);
    }
}