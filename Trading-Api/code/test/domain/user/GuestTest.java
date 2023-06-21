package domain.user;

import database.daos.Dao;
import domain.store.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.infoRelated.ProductInfo;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {
    private Guest guest;
    private Product apple;
    private ProductInfo p;
    private Product banana;
    private ProductInfo p2;

    @BeforeEach
    void setUp() {
        Dao.setForTests(true);
        apple = new Product(0, 0, "apple", "red apple", 50, 50);
        p =  new ProductInfo(0, apple, 10);
        banana = new Product(0, 1, "banana", "yellow banana", 50, 50);
        p2 =  new ProductInfo(0, banana, 10);
        guest = new Guest(2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProductToCart_success() {
        try {
            guest.addProductToCart(1, p, 100, null);
            ShoppingCart cart = guest.getShoppingCart();
            assertTrue(cart.getBasket(1) != null);
            Basket products = cart.getBasket(1);
            assertTrue(products.getProduct(0).quantity == 100);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void addProductToCart_fail() {
        try {
            guest.addProductToCart(1, p, -100, null);
            assertTrue(false);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void removeProductFromCart_success() {
        try{
            guest.addProductToCart(1, p, 100,null);
            guest.removeProductFromCart(1,0,null);
            ShoppingCart cart = guest.getShoppingCart();
            assertTrue(cart.getBasket(1)==null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void removeProductFromCart_fail() {
        try{
            guest.removeProductFromCart(0,0,null);
            assertTrue(false);
        }catch (Exception e){
            assertTrue(true);
        }
    }
    @Test
    void changeQuantityInCart_success() {
        try{
            guest.addProductToCart(1, p, 100,null);
            guest.changeQuantityInCart(1,p,10,null);
            ShoppingCart cart = guest.getShoppingCart();
            assertTrue(cart.getBasket(1)!=null);
            assertTrue(cart.getBasket(1).getProduct(0).quantity==110);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void changeQuantityInCart_fail() {
        try{
            guest.addProductToCart(0,p,-10,null);
            assertFalse(true);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void getCartContent_success() {
        try{
            guest.addProductToCart(1, p, 100,null);
            ShoppingCart cart = guest.getShoppingCart();
            assertTrue(cart.getBasket(1)!=null);
            assertTrue(cart.getBasket(1).getProduct(0).quantity==100);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void getCartContent_fail() {
        try{
            guest.addProductToCart(1, p, 100,null);
            ShoppingCart cart = guest.getShoppingCart();
            assertFalse(cart.getBasket(2)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }


    @Test
    void emptyCart_success() {
        try{
            guest.addProductToCart(1, p, 100,null);
            guest.emptyCart();
            ShoppingCart cart = guest.getShoppingCart();
            assertFalse(cart.getBasket(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void emptyCart_fail() {
        try{
            guest.addProductToCart(1, p, 100,null);
            guest.emptyCart();
            ShoppingCart cart = guest.getShoppingCart();
            assertFalse(cart.getBasket(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
}