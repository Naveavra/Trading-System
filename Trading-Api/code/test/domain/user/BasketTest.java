package domain.user;

import database.daos.Dao;
import domain.store.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.infoRelated.ProductInfo;

import static org.junit.jupiter.api.Assertions.*;

class BasketTest {
    private Basket basket;
    private Product apple;
    private ProductInfo p;
    private Product banana;
    private ProductInfo p2;

    @BeforeEach
    void setUp() {
        Dao.setForTests(true);
        basket = new Basket(0);
        apple = new Product(0, 0, "apple", "red apple", 50, 50);
        p =  new ProductInfo(0, apple, 10);
        banana = new Product(0, 1, "banana", "yellow banana", 50, 50);
        p2 =  new ProductInfo(0, banana, 10);
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