package domain.user;

import database.Dao;
import domain.store.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.infoRelated.ProductInfo;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BasketTest {
    private Basket basket;
    private Product apple;
    private ProductInfo p;
    private Product banana;
    private ProductInfo p2;

    @BeforeEach
    void setUp() {
        basket = new Basket(0);
        apple = new Product(0, 0, "apple", "red apple");
        p =  new ProductInfo(0, apple, 10);
        banana = new Product(0, 1, "banana", "yellow banana");
        p2 =  new ProductInfo(0, banana, 10);
        Dao.setForTests(true);
    }

    //product - int prod_id, String _name, String desc
    @AfterEach
    void tearDown() {
    }

    @Test
    void addProductToCart_success() {
        try {
            basket.addProductToCart(p, 10);
            basket.addProductToCart(p2, 10);
            assertTrue(basket.getProduct(0) != null);
            assertTrue(basket.getProduct(0).quantity == 10);
            assertTrue(basket.getContent().size() == 2);
        }catch (Exception e){
            assert false;
        }
    }

    @Test
    void addProductToCart_failed() {
        try {
            basket.changeQuantityInCart(p, -1);
            assertFalse(true);
        } catch (Exception e) {
            assert true;
        }
    }


    @Test
    void removeProductFromCart() {
        try {
            basket.addProductToCart(p, 10);
            basket.addProductToCart(p2, 10);
            basket.removeProductFromCart(0);
            assertTrue(basket.getProduct(0) == null);
            assertTrue(basket.getContent().size() == 1);
        }catch (Exception e){
            assert false;
        }
    }

    @Test
    void changeQuantityInCart() {
        try {
            basket.addProductToCart(p,10);
            basket.changeQuantityInCart(p, 100);
            assertTrue(basket.getProduct(0) != null);
            assertTrue(basket.getProduct(0).quantity == 110);
        }catch (Exception e){
            assert false;
        }
    }
}