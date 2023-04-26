package domain.store.storeManagement;

import domain.store.product.Product;
import domain.user.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.MarketController;
import service.UserController;
import utils.orderRelated.Order;
import utils.messageRelated.Message;
import utils.messageRelated.MessageState;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

class StoreTest {
    private Store store;

    Member user1;
    Member user2;
    MarketController marketCtrl;
    StoreController storeCtrl;
    UserController userCtrl;
    @BeforeEach
    void setUp() throws Exception {
        int storeId = 1;
        int creatorId = 1;
        String storeDescription = "This is a test store.";
        store = new Store(storeId, storeDescription, creatorId);
        userCtrl = new UserController();
        AtomicInteger pid = new AtomicInteger(0);
        store.addNewProduct("testproduct1", "test", pid);
        store.addNewProduct("testproduct2", "test", pid);
        store.setProductQuantity(0, 50);
        store.setProductQuantity(1, 50);
        store.setPrice(0, 10);
        store.setPrice(1, 5);
        HashMap<Integer,Integer> products1 = new HashMap<>();
        products1.put(0, 10); //pid, quantity
        products1.put(1, 5);
        HashMap<Integer, HashMap<Integer,Integer>> productinOrder1 = new HashMap<>(); //storeid, products
        productinOrder1.put(0, products1);
        Order order1 = new Order(0, 0, productinOrder1);
        store.addOrder(order1);
        user1 = new Member(1, "eliben123@gmail.com", "aBc123", "24/02/2002");
        storeCtrl = new StoreController();
        userCtrl.register("elliben123@gmail.com", "aBc1234", "21/02/2002");
        // marketCtrl.openStore(1, "another shitty store");
    }

    @Test
    void SetWrongPrice()
    {
        Assertions.assertThrows(Exception.class, () -> {
            store.setPrice(0, -5);
        });
    }

    @Test
    void SetNewPrice () throws Exception
    {
        store.setPrice(0, 1);
        store.setPrice(1, 1);
        Assertions.assertEquals(1, store.getInventory().getProduct(0).price);
        Assertions.assertEquals(1, store.getInventory().getProduct(1).price);
    }

    @Test
    void getProductNotInStore() throws Exception
    {
        Assertions.assertNull(store.getInventory().getProduct(3));
        Assertions.assertNull(store.getInventory().getProduct(2));
    }

    @Test
    void getProduct()
    {
        Assertions.assertEquals(0, store.getInventory().getProduct(0).id);
        Assertions.assertEquals(1, store.getInventory().getProduct(1).id);

    }

    @Test
    void addReview() throws Exception
    {
        Message msg = new Message(0, "what a shitty store", user1, 0, 0, MessageState.reviewStore);
        Order order = store.getOrdersHistory().get(0);
        store.addReview(order.getOrderId(), msg);
        Assertions.assertEquals(0, store.getOrdersHistory().get(0).getOrderId());
        Assertions.assertEquals("what a shitty store", store.checkMessages().get(0));

    }

    @Test
    void giveFeedback() throws Exception
    {
        Message msg = new Message(0, "what a shitty store", user1, 0, 0, MessageState.question);
        Order order = store.getOrdersHistory().get(0);
        store.addReview(order.getOrderId(), msg);
        Assertions.assertFalse(msg.gotFeedback());
        store.answerQuestion(0, "fuck you cuz");
        Assertions.assertTrue(msg.gotFeedback());
    }

    @Test
    void getMessagesEmpty() throws Exception
    {
        Message msg = new Message(0, "what a shitty store", user1, 0, 0, MessageState.reviewStore);
        Order order = store.getOrdersHistory().get(0);
        store.addReview(order.getOrderId(), msg);
        store.checkMessages();
        Assertions.assertTrue(store.checkMessages().isEmpty());
    }

//    @Test
//    void closeStoreTemporary() throws Exception {
//        Store store2 = storeCtrl.storeList.get(1);
//        Assertions.assertTrue(store2.closeStoreTemporary(1).contains(2));
//    }


//    @Test
//    void testAddNewProduct() {
//        // Test adding a new product to the inventory
//        String name = "Test Product";
//        String description = "This is a test product.";
//        AtomicInteger pid = new AtomicInteger(0);
//        Product product = store.addNewProduct(name, description, pid);
//        Assertions.assertEquals(product.getName(), name);
//        Assertions.assertEquals(product.getDescription(), description);
//        //        Assertions.assertEquals(product.getID(), pid.get());
//
//        // Test adding a new product with a Product object
//        Product newProduct = new Product(5, "test product", "BLAH BLAH");
//        Product addedProduct = store.addNewProduct(newProduct);
//        Assertions.assertEquals(addedProduct.getName(), newProduct.getName());
//        Assertions.assertEquals(addedProduct.getDescription(), newProduct.getDescription());
//        Assertions.assertEquals(addedProduct.getID(), newProduct.getID());
//    }

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
