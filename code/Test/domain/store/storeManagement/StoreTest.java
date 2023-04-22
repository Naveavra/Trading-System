package domain.store.storeManagement;

import domain.store.product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Message;
import utils.Pair;
import utils.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class StoreTest {
    private Store store;

    @BeforeEach
    void setUp() {
        int storeId = 1;
        int creatorId = 1;
        String storeDescription = "This is a test store.";
        store = new Store(storeId, storeDescription, creatorId);
    }

    @Test
    void testAddNewProduct() {
        // Test adding a new product to the inventory
        String name = "Test Product";
        String description = "This is a test product.";
        AtomicInteger pid = new AtomicInteger(0);
        Product product = store.addNewProduct(name, description, pid);
        Assertions.assertEquals(product.getName(), name);
        Assertions.assertEquals(product.getDescription(), description);
        //        Assertions.assertEquals(product.getID(), pid.get());

        // Test adding a new product with a Product object
        Product newProduct = new Product(5, "test product", "BLAH BLAH");
        Product addedProduct = store.addNewProduct(newProduct);
        Assertions.assertEquals(addedProduct.getName(), newProduct.getName());
        Assertions.assertEquals(addedProduct.getDescription(), newProduct.getDescription());
        Assertions.assertEquals(addedProduct.getID(), newProduct.getID());
    }

    @Test
    void testSetProductQuantity() throws Exception {
        // Test setting the quantity of a product
        String name = "Test Product";
        String description = "This is a test product.";
        AtomicInteger pid = new AtomicInteger(1);
        Product product = store.addNewProduct(name, description, pid);
        int quantityToAdd = 10;
        store.setProductQuantity(product.getID(), quantityToAdd);
        Assertions.assertEquals(store.getQuantityOfProduct(product.getID()), quantityToAdd);
    }

    @Test
    void testSetDescription() throws Exception {
        // Test setting the description of a product
        String name = "Test Product";
        String description = "This is a test product.";
        AtomicInteger pid = new AtomicInteger(1);
        Product product = store.addNewProduct(name, description, pid);
        String newDescription = "This is an updated description.";
        store.setDescription(product.getID(), newDescription);
        Assertions.assertEquals(store.getInventory().getProduct(product.getID()).getDescription(), newDescription);

        // Test trying to set the description of a product that doesn't exist
        Assertions.assertThrows(Exception.class, () -> {
            store.setDescription(100, newDescription);
        });
    }

    @Test
    void testSetPrice() throws Exception {
        // Test setting the price of a product
        String name = "Test Product";
        String description = "This is a test product.";
        AtomicInteger pid = new AtomicInteger(1);
        int price = 20;
        Product product = store.addNewProduct(name, description, pid);
        store.setPrice(product.getID(), price);
        Assertions.assertEquals(store.getInventory().getProduct(product.getID()).getPrice(), price);

        // Test trying to set the price of a product that doesn't exist
        Assertions.assertThrows(Exception.class, () -> {
            store.setPrice(100, 30);
        });
    }
}
