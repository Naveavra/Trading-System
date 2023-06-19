package store;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import database.daos.Dao;
import domain.store.product.Product;
import domain.user.Basket;
import domain.user.Guest;
import domain.user.ShoppingCart;
import utils.infoRelated.ProductInfo;
import utils.orderRelated.Order;
import org.junit.jupiter.api.*;



public class OrderTest {
    private Order order = new Order(0,new Guest(0),new ShoppingCart());
    private Product apple;
    private ProductInfo p;
    private Product banana;
    private ProductInfo p2;

    @Test
    void testAddProductsToOrder() throws Exception{
        Dao.setForTests(true);
        // Test that adds products to a new store
//        order.clean();
        int storeID = 1;
        apple = new Product(storeID, 0, "apple", "red apple");
        p =  new ProductInfo(storeID, apple, 10);
        banana = new Product(storeID, 1, "banana", "yellow banana");
        p2 =  new ProductInfo(storeID, banana, 10);
        Basket products = new Basket(0);
        products.addProductToCart(p, 101);
        products.addProductToCart(p2, 102);
        Order order = this.order;
        order.addProductsToOrder(storeID, products.getContent());
        HashMap<Integer, List<ProductInfo>> expected = new HashMap<>();
        expected.put(storeID, products.getContent());
        assertEquals(expected.get(storeID).size(), order.getProductsInStores().size());
    }

    @Test
    void testAddProductsToOrderFail() {
        Dao.setForTests(true);
        // Test that fails because of a wrong input parameter
//        order.clean();
        try {
            int storeID = -1;
            apple = new Product(storeID, 0, "apple", "red apple");
            p =  new ProductInfo(storeID, apple, 10);
            banana = new Product(storeID, 1, "banana", "yellow banana");
            p2 =  new ProductInfo(storeID, banana, 10);
            Basket products = new Basket(0);
            products.addProductToCart(p, 10);
            products.addProductToCart(p2, 5);
            Order order = this.order;
            Assertions.assertThrows(Exception.class, () -> {
                order.addProductsToOrder(storeID, products.getContent());
            });
        }catch (Exception e){
            assert false;
        }
    }


}
