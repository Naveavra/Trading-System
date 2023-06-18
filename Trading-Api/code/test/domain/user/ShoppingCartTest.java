package domain.user;

import database.daos.Dao;
import domain.store.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.infoRelated.ProductInfo;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    Guest g ;
    ShoppingCart s ;
    private Product apple;
    private ProductInfo p;
    private Product banana;
    private ProductInfo p2;
    @BeforeEach
    void setUp() {
        Dao.setForTests(true);
        g = new Guest(1);
        s = new ShoppingCart();
        apple = new Product(0, 0, "apple", "red apple");
        p =  new ProductInfo(0, apple, 100);
        banana = new Product(0, 1, "banana", "yellow banana");
        p2 =  new ProductInfo(1, banana, 50);
    }

    @Test
    void addProductToCart_success() {
        try {
            s.addProductToCart(1, p, 100);
            assertTrue(s.getBasket(1) != null);
            Basket products = s.getBasket(1);
            assertTrue(products.getProduct(0).quantity == 100);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void addProductToCart_fail() {
        try {
            s.addProductToCart(1, p, -100);
            assertTrue(false);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void removeProductFromCart_success() {
        try{
            s.addProductToCart(1, p, 100);
            s.removeProductFromCart(1,p.getId());
            assertTrue(s.getBasket(1)==null);
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
            s.addProductToCart(1, p, 100);
            s.changeQuantityInCart(1,p,10);
            assertTrue(s.getBasket(1)!=null);
            assertTrue(s.getBasket(1).getProduct(0).quantity==110);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void changeQuantityInCart_fail() {
        try{
            s.changeQuantityInCart(0,p,-100);
            assert false;
        }catch (Exception e){
            assert true;
        }
    }

    @Test
    void getContent_success() {
        try{
            s.addProductToCart(1, p, 100);
            assertTrue(s.getBasket(1)!=null);
            assertTrue(s.getBasket(1).getProduct(0).quantity==100);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void getContent_fail() {
        try{
            s.addProductToCart(1, p, 100);
            assertFalse(s.getBasket(2)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void emptyCart_success() {
        try{
            s.addProductToCart(1, p, 100);
            s.emptyCart();
            assertFalse(s.getBasket(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void emptyCart_fail() {
        try{
            s.addProductToCart(1, p, 100);
            s.emptyCart();
            assertFalse(s.getBasket(1)!=null);
        }catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void changeQuantity0AndRemoveProduct(){
        try {
            s.changeQuantityInCart(1, p, 100);
            assertEquals(1, s.getContent().size());
            s.changeQuantityInCart(1, p2, 50);
            assertEquals(2, s.getContent().size());
            s.changeQuantityInCart(1, p2, -50);
            assertEquals(1, s.getContent().size());
        }catch (Exception e){
            assert false;
        }
    }
}