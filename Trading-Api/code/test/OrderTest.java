package store;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import utils.orderRelated.Order;
import org.junit.jupiter.api.*;



public class OrderTest {
    private Order order = new Order(0,0,new HashMap<>());

    @Test
    void testAddProductsToOrder() throws Exception{
        // Test that adds products to a new store
//        order.clean();
        int storeID = 1;
        HashMap<Integer, Integer> products = new HashMap<>();
        products.put(101, 10);
        products.put(102, 5);
        Order order = this.order;
        order.addProductsToOrder(storeID, products);
        HashMap<Integer, HashMap<Integer, Integer>> expected = new HashMap<>();
        expected.put(storeID, products);
        assertEquals(expected, order.getProductsInStores());
    }

    @Test
    void testAddProductsToOrderFail() {
        // Test that fails because of a wrong input parameter
//        order.clean();
        int storeID = -1;
        HashMap<Integer, Integer> products = new HashMap<>();
        products.put(101, 10);
        products.put(102, 5);
        Order order =this.order;
        Assertions.assertThrows(Exception.class, () -> {
            order.addProductsToOrder(storeID, products);
        });
    }


}
